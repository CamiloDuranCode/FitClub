-- =============================================================================
-- FITCLUB — 06_procedimientos.sql
-- Stored procedures para operaciones de negocio complejas
-- =============================================================================

-- =============================================================================
-- SP 1: Registrar cliente nuevo
-- Inserta en persona y cliente en una sola transacción
-- =============================================================================
CREATE OR REPLACE PROCEDURE sp_registrar_cliente(
    p_cedula           VARCHAR,
    p_nombre           VARCHAR,
    p_telefono         VARCHAR,
    p_fecha_nacimiento DATE,
    p_direccion        VARCHAR,
    p_email            VARCHAR DEFAULT NULL
)
LANGUAGE plpgsql AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM persona WHERE cedula = p_cedula) THEN
        RAISE EXCEPTION 'Ya existe una persona registrada con la cédula %', p_cedula;
    END IF;

    INSERT INTO persona (cedula, nombre, telefono)
    VALUES (p_cedula, p_nombre, p_telefono);

    INSERT INTO cliente (cedula, fecha_nacimiento, direccion, email)
    VALUES (p_cedula, p_fecha_nacimiento, p_direccion, p_email);

    RAISE NOTICE 'Cliente % registrado correctamente.', p_nombre;
END;
$$;

-- =============================================================================
-- SP 2: Desactivar cliente (soft delete)
-- Cambia activo = FALSE sin eliminar el registro ni su historial
-- =============================================================================
CREATE OR REPLACE PROCEDURE sp_desactivar_cliente(p_cedula VARCHAR)
LANGUAGE plpgsql AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM cliente WHERE cedula = p_cedula) THEN
        RAISE EXCEPTION 'El cliente con cédula % no existe.', p_cedula;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM cliente WHERE cedula = p_cedula AND activo = TRUE) THEN
        RAISE EXCEPTION 'El cliente con cédula % ya está desactivado.', p_cedula;
    END IF;

    UPDATE cliente SET activo = FALSE WHERE cedula = p_cedula;

    -- Cancelar membresías activas al desactivar el cliente
    UPDATE cliente_membresia
    SET estado = 'cancelada'
    WHERE cedula = p_cedula AND estado = 'activa';

    RAISE NOTICE 'Cliente % desactivado. Membresías activas canceladas.', p_cedula;
END;
$$;

-- =============================================================================
-- SP 3: Asignar membresía a un cliente
-- Suspende la membresía anterior si existe, crea la nueva y registra el pago
-- RN-06: Un cliente solo puede tener UNA membresía activa simultáneamente
-- =============================================================================
CREATE OR REPLACE PROCEDURE sp_asignar_membresia(
    p_cedula       VARCHAR,
    p_id_membresia INT,
    p_metodo_pago  metodo_pago DEFAULT 'efectivo'
)
LANGUAGE plpgsql AS $$
DECLARE
    v_duracion    INT;
    v_costo       NUMERIC(10,2);
    v_tipo        TEXT;
    v_inicio      DATE := CURRENT_DATE;
    v_fin         DATE;
BEGIN
    -- Verificar que el cliente exista y esté activo
    IF NOT EXISTS (SELECT 1 FROM cliente WHERE cedula = p_cedula AND activo = TRUE) THEN
        RAISE EXCEPTION 'El cliente con cédula % no existe o está inactivo.', p_cedula;
    END IF;

    -- Obtener datos de la membresía
    SELECT duracion_dias, costo, tipo::TEXT
    INTO v_duracion, v_costo, v_tipo
    FROM membresia
    WHERE id_membresia = p_id_membresia AND activa = TRUE;

    IF v_duracion IS NULL THEN
        RAISE EXCEPTION 'La membresía % no existe o no está activa.', p_id_membresia;
    END IF;

    v_fin := v_inicio + v_duracion;

    -- Cancelar membresía activa anterior
    UPDATE cliente_membresia
    SET estado = 'cancelada'
    WHERE cedula = p_cedula AND estado = 'activa';

    -- Crear nueva membresía
    INSERT INTO cliente_membresia (cedula, id_membresia, fecha_inicio, fecha_fin, estado)
    VALUES (p_cedula, p_id_membresia, v_inicio, v_fin, 'activa');

    -- Registrar pago automáticamente
    INSERT INTO pago (cedula, monto, metodo_pago, concepto)
    VALUES (p_cedula, v_costo, p_metodo_pago,
            'Membresía ' || v_tipo || ' — vence ' || v_fin::TEXT);

    RAISE NOTICE 'Membresía % asignada a cliente %. Vigente hasta %. Pago de $% registrado.',
        v_tipo, p_cedula, v_fin, v_costo;
END;
$$;

-- =============================================================================
-- SP 4: Registrar asistencia (entrada o salida)
-- Delega la validación de secuencia al trigger trg_validar_asistencia
-- =============================================================================
CREATE OR REPLACE PROCEDURE sp_registrar_asistencia(
    p_cedula      VARCHAR,
    p_tipo        tipo_asistencia,
    p_observacion TEXT DEFAULT NULL
)
LANGUAGE plpgsql AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM cliente WHERE cedula = p_cedula AND activo = TRUE) THEN
        RAISE EXCEPTION 'El cliente con cédula % no existe o está inactivo.', p_cedula;
    END IF;

    INSERT INTO asistencia (cedula, tipo, observacion)
    VALUES (p_cedula, p_tipo, p_observacion);

    RAISE NOTICE '% registrada para cliente % a las %', p_tipo, p_cedula, NOW();
END;
$$;

-- =============================================================================
-- SP 5: Iniciar uso de máquina
-- Valida disponibilidad y registra el inicio (el trigger gestiona el estado)
-- =============================================================================
CREATE OR REPLACE PROCEDURE sp_iniciar_uso_maquina(
    p_cedula     VARCHAR,
    p_id_maquina INT
)
LANGUAGE plpgsql AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM cliente WHERE cedula = p_cedula AND activo = TRUE) THEN
        RAISE EXCEPTION 'El cliente con cédula % no existe o está inactivo.', p_cedula;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM maquina WHERE id_maquina = p_id_maquina AND activa = TRUE) THEN
        RAISE EXCEPTION 'La máquina % no existe o no está activa.', p_id_maquina;
    END IF;

    INSERT INTO uso_maquina (id_maquina, cedula, fecha_hora_inicio)
    VALUES (p_id_maquina, p_cedula, CURRENT_TIMESTAMP);

    RAISE NOTICE 'Uso de máquina % iniciado para cliente %.', p_id_maquina, p_cedula;
END;
$$;

-- =============================================================================
-- SP 6: Finalizar uso de máquina
-- Registra la hora de fin (el trigger actualiza el estado a 'disponible')
-- =============================================================================
CREATE OR REPLACE PROCEDURE sp_finalizar_uso_maquina(p_id_uso INT)
LANGUAGE plpgsql AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM uso_maquina WHERE id_uso = p_id_uso AND fecha_hora_fin IS NULL) THEN
        RAISE EXCEPTION 'No existe un uso activo con ID %.', p_id_uso;
    END IF;

    UPDATE uso_maquina
    SET fecha_hora_fin = CURRENT_TIMESTAMP
    WHERE id_uso = p_id_uso;

    RAISE NOTICE 'Uso % finalizado correctamente.', p_id_uso;
END;
$$;

-- =============================================================================
-- SP 7: Registrar usuario del sistema
-- =============================================================================
CREATE OR REPLACE PROCEDURE sp_registrar_usuario(
    p_username          VARCHAR,
    p_password          VARCHAR,
    p_rol               rol_usuario,
    p_nombre            VARCHAR,
    p_cedula_entrenador VARCHAR DEFAULT NULL
)
LANGUAGE plpgsql AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM usuario WHERE username = p_username) THEN
        RAISE EXCEPTION 'El usuario "%" ya existe.', p_username;
    END IF;

    IF LENGTH(p_password) < 6 THEN
        RAISE EXCEPTION 'La contraseña debe tener al menos 6 caracteres.';
    END IF;

    IF p_rol = 'entrenador' AND p_cedula_entrenador IS NULL THEN
        RAISE EXCEPTION 'Un usuario con rol entrenador debe tener cedula_entrenador asignada.';
    END IF;

    IF p_cedula_entrenador IS NOT NULL AND
       NOT EXISTS (SELECT 1 FROM entrenador WHERE cedula = p_cedula_entrenador) THEN
        RAISE EXCEPTION 'No existe entrenador con cédula %.', p_cedula_entrenador;
    END IF;

    INSERT INTO usuario (username, password, rol, nombre, cedula_entrenador)
    VALUES (p_username, p_password, p_rol, p_nombre, p_cedula_entrenador);

    RAISE NOTICE 'Usuario "%" creado con rol %.', p_username, p_rol;
END;
$$;

-- =============================================================================
-- SP 8: Cambiar contraseña de usuario
-- Recibe el nuevo hash SHA-256 ya calculado desde Java
-- =============================================================================
CREATE OR REPLACE PROCEDURE sp_cambiar_password(
    p_id_usuario INT,
    p_password   VARCHAR
)
LANGUAGE plpgsql AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM usuario WHERE id_usuario = p_id_usuario) THEN
        RAISE EXCEPTION 'No existe usuario con ID %.', p_id_usuario;
    END IF;

    IF LENGTH(p_password) < 6 THEN
        RAISE EXCEPTION 'La contraseña debe tener al menos 6 caracteres.';
    END IF;

    UPDATE usuario
    SET password = p_password
    WHERE id_usuario = p_id_usuario;

    RAISE NOTICE 'Contraseña actualizada para usuario ID %.', p_id_usuario;
END;
$$;

-- =============================================================================
-- SP 9: Activar/desactivar usuario del sistema
-- =============================================================================
CREATE OR REPLACE PROCEDURE sp_toggle_usuario(p_id_usuario INT)
LANGUAGE plpgsql AS $$
DECLARE
    v_estado BOOLEAN;
BEGIN
    SELECT activo INTO v_estado FROM usuario WHERE id_usuario = p_id_usuario;

    IF v_estado IS NULL THEN
        RAISE EXCEPTION 'No existe usuario con ID %.', p_id_usuario;
    END IF;

    UPDATE usuario SET activo = NOT activo WHERE id_usuario = p_id_usuario;

    RAISE NOTICE 'Usuario % %.',
        p_id_usuario,
        CASE WHEN NOT v_estado THEN 'activado' ELSE 'desactivado' END;
END;
$$;

-- =============================================================================
-- SP 10: Asignar entrenador a cliente
-- Usa ON CONFLICT para evitar duplicados silenciosamente
-- =============================================================================
CREATE OR REPLACE PROCEDURE sp_asignar_cliente_entrenador(
    p_cedula_cliente    VARCHAR,
    p_cedula_entrenador VARCHAR
)
LANGUAGE plpgsql AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM cliente WHERE cedula = p_cedula_cliente AND activo = TRUE) THEN
        RAISE EXCEPTION 'El cliente con cédula % no existe o está inactivo.', p_cedula_cliente;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM entrenador WHERE cedula = p_cedula_entrenador AND activo = TRUE) THEN
        RAISE EXCEPTION 'El entrenador con cédula % no existe o está inactivo.', p_cedula_entrenador;
    END IF;

    INSERT INTO cliente_entrenador (cedula_cliente, cedula_entrenador)
    VALUES (p_cedula_cliente, p_cedula_entrenador)
    ON CONFLICT (cedula_cliente, cedula_entrenador) DO NOTHING;

    RAISE NOTICE 'Cliente % asignado al entrenador %.', p_cedula_cliente, p_cedula_entrenador;
END;
$$;

-- =============================================================================
-- SP 11: Registrar progreso físico de un cliente
-- El trigger fn_calcular_imc calcula el IMC automáticamente
-- =============================================================================
CREATE OR REPLACE PROCEDURE sp_registrar_progreso(
    p_cedula        VARCHAR,
    p_peso_kg       NUMERIC,
    p_altura_m      NUMERIC,
    p_observaciones TEXT DEFAULT NULL
)
LANGUAGE plpgsql AS $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM cliente WHERE cedula = p_cedula AND activo = TRUE) THEN
        RAISE EXCEPTION 'El cliente con cédula % no existe o está inactivo.', p_cedula;
    END IF;

    INSERT INTO progreso (cedula, peso_kg, altura_m, observaciones)
    VALUES (p_cedula, p_peso_kg, p_altura_m, p_observaciones);

    RAISE NOTICE 'Progreso registrado. Peso: % kg | Altura: % m', p_peso_kg, p_altura_m;
END;
$$;

-- =============================================================================
-- SP 12: Actualizar membresías vencidas (llamar al iniciar la aplicación)
-- Equivalente a MembresiaService.actualizarEstadosVencidos()
-- =============================================================================
CREATE OR REPLACE PROCEDURE sp_actualizar_membresias_vencidas()
LANGUAGE plpgsql AS $$
DECLARE
    v_count INT;
BEGIN
    UPDATE cliente_membresia
    SET estado = 'vencida'
    WHERE estado = 'activa'
      AND fecha_fin < CURRENT_DATE;

    GET DIAGNOSTICS v_count = ROW_COUNT;
    RAISE NOTICE '% membresía(s) actualizadas a estado vencida.', v_count;
END;
$$;
