-- =============================================================================
-- FITCLUB — 05_funciones.sql
-- Funciones y triggers de la base de datos
-- =============================================================================

-- =============================================================================
-- FUNCIÓN 1 + TRIGGER: Cálculo automático de IMC al registrar progreso
-- RN-01: El IMC se calcula automáticamente en INSERT/UPDATE de progreso
-- =============================================================================
CREATE OR REPLACE FUNCTION fn_calcular_imc()
RETURNS TRIGGER LANGUAGE plpgsql AS $$
BEGIN
    IF NEW.altura_m IS NOT NULL AND NEW.altura_m > 0 THEN
        NEW.imc := ROUND(NEW.peso_kg / (NEW.altura_m * NEW.altura_m), 2);
    END IF;
    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_calcular_imc
BEFORE INSERT OR UPDATE ON progreso
FOR EACH ROW EXECUTE FUNCTION fn_calcular_imc();

-- =============================================================================
-- FUNCIÓN 2 + TRIGGER: Validación de secuencia de asistencia entrada/salida
-- RN-02: No se puede registrar salida sin entrada previa ese día.
--        No se puede repetir el mismo tipo de asistencia consecutivamente.
-- =============================================================================
CREATE OR REPLACE FUNCTION fn_validar_asistencia()
RETURNS TRIGGER LANGUAGE plpgsql AS $$
DECLARE
    v_ultimo_tipo tipo_asistencia;
BEGIN
    SELECT tipo INTO v_ultimo_tipo
    FROM asistencia
    WHERE cedula     = NEW.cedula
      AND DATE(fecha_hora) = DATE(NEW.fecha_hora)
    ORDER BY fecha_hora DESC
    LIMIT 1;

    IF v_ultimo_tipo IS NULL AND NEW.tipo = 'salida' THEN
        RAISE EXCEPTION
            'RN-02: El cliente % no puede registrar salida sin entrada previa hoy.',
            NEW.cedula;
    END IF;

    IF v_ultimo_tipo IS NOT NULL AND v_ultimo_tipo = NEW.tipo THEN
        RAISE EXCEPTION
            'RN-02: El cliente % ya tiene "%" registrada hoy. Registre "%" primero.',
            NEW.cedula,
            NEW.tipo,
            CASE WHEN NEW.tipo = 'entrada' THEN 'salida' ELSE 'entrada' END;
    END IF;

    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_validar_asistencia
BEFORE INSERT ON asistencia
FOR EACH ROW EXECUTE FUNCTION fn_validar_asistencia();

-- =============================================================================
-- FUNCIÓN 3 + TRIGGER: Gestión automática del estado de máquinas
-- RN-03: Al iniciar uso → estado 'en_uso'. Al terminar → 'disponible'.
--        No se puede usar una máquina en mantenimiento.
-- =============================================================================
CREATE OR REPLACE FUNCTION fn_gestionar_estado_maquina()
RETURNS TRIGGER LANGUAGE plpgsql AS $$
DECLARE
    v_estado estado_maquina;
BEGIN
    SELECT estado INTO v_estado
    FROM maquina
    WHERE id_maquina = NEW.id_maquina;

    IF TG_OP = 'INSERT' THEN
        IF v_estado = 'mantenimiento' THEN
            RAISE EXCEPTION
                'RN-03: La máquina % está en mantenimiento y no puede ser usada.',
                NEW.id_maquina;
        END IF;
        IF v_estado = 'en_uso' THEN
            RAISE EXCEPTION
                'RN-03: La máquina % ya está en uso por otro cliente.',
                NEW.id_maquina;
        END IF;
        UPDATE maquina SET estado = 'en_uso' WHERE id_maquina = NEW.id_maquina;
    END IF;

    IF TG_OP = 'UPDATE' AND NEW.fecha_hora_fin IS NOT NULL AND OLD.fecha_hora_fin IS NULL THEN
        UPDATE maquina SET estado = 'disponible' WHERE id_maquina = NEW.id_maquina;
    END IF;

    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_gestionar_maquina
BEFORE INSERT OR UPDATE ON uso_maquina
FOR EACH ROW EXECUTE FUNCTION fn_gestionar_estado_maquina();

-- =============================================================================
-- FUNCIÓN 4: Login de usuario (retorna datos del usuario autenticado)
-- RN-04: Autenticación con username + password en texto plano (mínimo 6 caracteres).
--        Solo usuarios activos pueden iniciar sesión.
-- =============================================================================
CREATE OR REPLACE FUNCTION fn_login(
    p_username VARCHAR,
    p_password VARCHAR
)
RETURNS TABLE(
    id_usuario        INT,
    username          VARCHAR,
    rol               rol_usuario,
    nombre            VARCHAR,
    cedula_entrenador VARCHAR
)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY
    SELECT
        u.id_usuario,
        u.username,
        u.rol,
        u.nombre,
        u.cedula_entrenador
    FROM usuario u
    WHERE u.username = p_username
      AND u.password = p_password
      AND u.activo   = TRUE;
END;
$$;

-- =============================================================================
-- FUNCIÓN 5: Actualización automática de membresías vencidas
-- RN-05: Cualquier membresía con fecha_fin < hoy y estado 'activa' pasa a 'vencida'.
--        Se recomienda llamar a esta función al iniciar la aplicación.
-- =============================================================================
CREATE OR REPLACE FUNCTION fn_actualizar_membresias_vencidas()
RETURNS INT LANGUAGE plpgsql AS $$
DECLARE
    v_count INT;
BEGIN
    UPDATE cliente_membresia
    SET estado = 'vencida'
    WHERE estado = 'activa'
      AND fecha_fin < CURRENT_DATE;

    GET DIAGNOSTICS v_count = ROW_COUNT;

    RAISE NOTICE 'RN-05: % membresía(s) actualizadas a estado vencida.', v_count;
    RETURN v_count;
END;
$$;

-- =============================================================================
-- FUNCIÓN 6: Obtener estado actual de la membresía de un cliente
-- Retorna: 'VIGENTE', 'POR VENCER' (≤7 días) o 'VENCIDA'
-- =============================================================================
CREATE OR REPLACE FUNCTION fn_estado_membresia(p_cedula VARCHAR)
RETURNS VARCHAR LANGUAGE plpgsql AS $$
DECLARE
    v_fecha_fin DATE;
    v_dias      INT;
BEGIN
    SELECT fecha_fin INTO v_fecha_fin
    FROM cliente_membresia
    WHERE cedula = p_cedula
      AND estado = 'activa'
    ORDER BY fecha_fin DESC
    LIMIT 1;

    IF v_fecha_fin IS NULL THEN
        RETURN 'SIN MEMBRESÍA';
    END IF;

    v_dias := v_fecha_fin - CURRENT_DATE;

    IF v_dias < 0 THEN
        RETURN 'VENCIDA';
    ELSIF v_dias <= 7 THEN
        RETURN 'POR VENCER';
    ELSE
        RETURN 'VIGENTE';
    END IF;
END;
$$;

-- =============================================================================
-- FUNCIÓN 7: Contar asistencias de un cliente en los últimos N días
-- Uso: ReporteService, AsistenciaService
-- =============================================================================
CREATE OR REPLACE FUNCTION fn_contar_asistencias(
    p_cedula   VARCHAR,
    p_dias     INT DEFAULT 30
)
RETURNS INT LANGUAGE plpgsql AS $$
DECLARE
    v_count INT;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM asistencia
    WHERE cedula  = p_cedula
      AND tipo    = 'entrada'
      AND fecha_hora >= CURRENT_DATE - (p_dias || ' days')::INTERVAL;
    RETURN v_count;
END;
$$;
