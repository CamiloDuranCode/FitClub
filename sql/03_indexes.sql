-- =============================================================================
-- FITCLUB — 03_indexes.sql
-- Índices para optimización de consultas frecuentes
-- =============================================================================

-- ===== CLIENTE =====
CREATE INDEX idx_cliente_email          ON cliente(email);
CREATE INDEX idx_cliente_activo         ON cliente(activo);
CREATE INDEX idx_cliente_fecha_registro ON cliente(fecha_registro);

-- ===== ENTRENADOR =====
CREATE INDEX idx_entrenador_activo      ON entrenador(activo);

-- ===== MEMBRESÍA =====
CREATE INDEX idx_membresia_tipo         ON membresia(tipo);
CREATE INDEX idx_membresia_activa       ON membresia(activa);

-- ===== CLIENTE MEMBRESÍA =====
CREATE INDEX idx_cli_mem_cedula         ON cliente_membresia(cedula);
CREATE INDEX idx_cli_mem_estado         ON cliente_membresia(estado);
CREATE INDEX idx_cli_mem_fecha_fin      ON cliente_membresia(fecha_fin);
-- Índice compuesto para búsqueda de membresías activas por vencer
CREATE INDEX idx_cli_mem_activa_fin     ON cliente_membresia(cedula, fecha_fin)
    WHERE estado = 'activa';

-- ===== CLIENTE ENTRENADOR =====
CREATE INDEX idx_cli_ent_cliente        ON cliente_entrenador(cedula_cliente);
CREATE INDEX idx_cli_ent_entrenador     ON cliente_entrenador(cedula_entrenador);

-- ===== TURNO ENTRENADOR =====
CREATE INDEX idx_turno_entrenador       ON turno_entrenador(cedula_entrenador);
CREATE INDEX idx_turno_dia              ON turno_entrenador(dia);

-- ===== RUTINA =====
CREATE INDEX idx_rutina_cliente         ON rutina(cedula_cliente);
CREATE INDEX idx_rutina_entrenador      ON rutina(cedula_entrenador);

-- ===== EJERCICIO =====
CREATE INDEX idx_ejercicio_rutina       ON ejercicio(id_rutina);
CREATE INDEX idx_ejercicio_maquina      ON ejercicio(id_maquina);

-- ===== PAGO =====
CREATE INDEX idx_pago_cedula            ON pago(cedula);
CREATE INDEX idx_pago_fecha             ON pago(fecha_pago DESC);
CREATE INDEX idx_pago_metodo            ON pago(metodo_pago);
-- Índice compuesto para reportes de ingresos por período
CREATE INDEX idx_pago_cedula_fecha      ON pago(cedula, fecha_pago DESC);

-- ===== ASISTENCIA =====
CREATE INDEX idx_asistencia_cedula      ON asistencia(cedula);
CREATE INDEX idx_asistencia_fecha       ON asistencia(fecha_hora DESC);
CREATE INDEX idx_asistencia_tipo        ON asistencia(tipo);
-- Índice compuesto para consultas de historial por cliente y fecha
CREATE INDEX idx_asistencia_cli_fecha   ON asistencia(cedula, fecha_hora DESC);

-- ===== USO MAQUINA =====
CREATE INDEX idx_uso_maquina_id         ON uso_maquina(id_maquina);
CREATE INDEX idx_uso_maquina_cedula     ON uso_maquina(cedula);
CREATE INDEX idx_uso_maquina_inicio     ON uso_maquina(fecha_hora_inicio DESC);
-- Índice parcial para usos activos (sin fecha de fin)
CREATE INDEX idx_uso_maquina_activo     ON uso_maquina(id_maquina)
    WHERE fecha_hora_fin IS NULL;

-- ===== PROGRESO =====
CREATE INDEX idx_progreso_cedula        ON progreso(cedula);
CREATE INDEX idx_progreso_fecha         ON progreso(fecha DESC);

-- ===== MAQUINA =====
CREATE INDEX idx_maquina_estado         ON maquina(estado);
CREATE INDEX idx_maquina_tipo           ON maquina(tipo);

-- ===== USUARIO =====
CREATE INDEX idx_usuario_username       ON usuario(username);
CREATE INDEX idx_usuario_rol            ON usuario(rol);
CREATE INDEX idx_usuario_activo         ON usuario(activo);
CREATE INDEX idx_usuario_entrenador     ON usuario(cedula_entrenador);
