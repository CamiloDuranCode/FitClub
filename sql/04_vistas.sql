-- =============================================================================
-- FITCLUB — 04_vistas.sql
-- Vistas para consultas frecuentes del sistema
-- =============================================================================

-- =============================================================================
-- VISTA 1: Membresías activas con días restantes
-- Uso: módulo de membresías, alertas de vencimiento
-- =============================================================================
CREATE OR REPLACE VIEW v_membresias_activas AS
    SELECT
        p.cedula,
        p.nombre,
        p.telefono,
        c.email,
        m.tipo,
        m.costo,
        m.beneficios,
        cm.fecha_inicio,
        cm.fecha_fin,
        cm.estado,
        (cm.fecha_fin - CURRENT_DATE) AS dias_restantes
    FROM cliente_membresia cm
    JOIN cliente    c ON c.cedula       = cm.cedula
    JOIN persona    p ON p.cedula       = c.cedula
    JOIN membresia  m ON m.id_membresia = cm.id_membresia
    WHERE cm.estado = 'activa'
      AND c.activo  = TRUE;

-- =============================================================================
-- VISTA 2: Membresías próximas a vencer (en los próximos 7 días)
-- Uso: alertas automáticas en el dashboard
-- =============================================================================
CREATE OR REPLACE VIEW v_membresias_por_vencer AS
    SELECT
        p.cedula,
        p.nombre,
        c.email,
        m.tipo,
        cm.fecha_fin,
        (cm.fecha_fin - CURRENT_DATE) AS dias_restantes
    FROM cliente_membresia cm
    JOIN cliente    c ON c.cedula       = cm.cedula
    JOIN persona    p ON p.cedula       = c.cedula
    JOIN membresia  m ON m.id_membresia = cm.id_membresia
    WHERE cm.estado = 'activa'
      AND cm.fecha_fin BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '7 days'
    ORDER BY cm.fecha_fin ASC;

-- =============================================================================
-- VISTA 3: Ingresos mensuales agrupados
-- Uso: módulo de reportes financieros (ReporteService)
-- =============================================================================
CREATE OR REPLACE VIEW v_ingresos_mensuales AS
    SELECT
        TO_CHAR(fecha_pago, 'YYYY-MM') AS mes,
        metodo_pago,
        SUM(monto)                     AS total_ingresos,
        COUNT(*)                       AS num_pagos
    FROM pago
    GROUP BY mes, metodo_pago
    ORDER BY mes DESC, total_ingresos DESC;

-- =============================================================================
-- VISTA 4: Ingresos por tipo de membresía
-- Uso: ReporteService.ingresosPorTipoMembresia()
-- =============================================================================
CREATE OR REPLACE VIEW v_ingresos_por_tipo AS
    SELECT
        m.tipo,
        TO_CHAR(p.fecha_pago, 'YYYY-MM') AS mes,
        SUM(p.monto)                      AS total,
        COUNT(*)                          AS num_pagos
    FROM pago p
    JOIN cliente_membresia cm ON cm.cedula = p.cedula
    JOIN membresia          m  ON m.id_membresia = cm.id_membresia
    WHERE p.fecha_pago::DATE BETWEEN cm.fecha_inicio AND cm.fecha_fin
    GROUP BY m.tipo, mes
    ORDER BY mes DESC, total DESC;

-- =============================================================================
-- VISTA 5: Clientes por entrenador con estado de membresía
-- Uso: módulo de entrenadores
-- =============================================================================
CREATE OR REPLACE VIEW v_clientes_por_entrenador AS
    SELECT
        pe.cedula                       AS cedula_entrenador,
        pe.nombre                       AS entrenador,
        e.especialidad,
        pc.cedula                       AS cedula_cliente,
        pc.nombre                       AS cliente,
        c.email,
        ce.fecha_asignacion
    FROM cliente_entrenador ce
    JOIN entrenador e  ON e.cedula = ce.cedula_entrenador
    JOIN persona    pe ON pe.cedula = e.cedula
    JOIN cliente    c  ON c.cedula  = ce.cedula_cliente
    JOIN persona    pc ON pc.cedula = c.cedula
    WHERE c.activo = TRUE
    ORDER BY pe.nombre, pc.nombre;

-- =============================================================================
-- VISTA 6: Turnos de entrenadores por día
-- Uso: módulo de entrenadores — gestión de horarios
-- =============================================================================
CREATE OR REPLACE VIEW v_turnos_entrenadores AS
    SELECT
        p.cedula                         AS cedula_entrenador,
        p.nombre                         AS entrenador,
        e.especialidad,
        t.dia,
        t.hora_inicio,
        t.hora_fin,
        (t.hora_fin - t.hora_inicio)     AS duracion
    FROM turno_entrenador t
    JOIN entrenador e ON e.cedula  = t.cedula_entrenador
    JOIN persona    p ON p.cedula  = e.cedula
    ORDER BY p.nombre, t.dia;

-- =============================================================================
-- VISTA 7: Estado actual de las máquinas
-- Uso: módulo de máquinas
-- =============================================================================
CREATE OR REPLACE VIEW v_estado_maquinas AS
    SELECT
        m.id_maquina,
        m.nombre,
        m.tipo,
        m.ubicacion,
        m.estado,
        um.cedula                         AS cedula_cliente_actual,
        p.nombre                          AS cliente_actual,
        um.fecha_hora_inicio              AS en_uso_desde
    FROM maquina m
    LEFT JOIN uso_maquina um ON um.id_maquina = m.id_maquina
                             AND um.fecha_hora_fin IS NULL
    LEFT JOIN cliente c ON c.cedula = um.cedula
    LEFT JOIN persona p ON p.cedula = c.cedula
    WHERE m.activa = TRUE
    ORDER BY m.estado, m.nombre;

-- =============================================================================
-- VISTA 8: Historial de asistencia con pares entrada/salida
-- Uso: módulo de asistencia
-- =============================================================================
CREATE OR REPLACE VIEW v_historial_asistencia AS
    SELECT
        a.id_asistencia,
        p.cedula,
        p.nombre,
        a.fecha_hora,
        a.tipo,
        a.observacion,
        DATE(a.fecha_hora) AS fecha
    FROM asistencia a
    JOIN cliente c ON c.cedula = a.cedula
    JOIN persona p ON p.cedula = c.cedula
    ORDER BY a.fecha_hora DESC;

-- =============================================================================
-- VISTA 9: Rutinas con detalle de ejercicios
-- Uso: módulo de rutinas
-- =============================================================================
CREATE OR REPLACE VIEW v_rutinas_con_ejercicios AS
    SELECT
        r.id_rutina,
        pc.nombre                         AS cliente,
        pe.nombre                         AS entrenador,
        r.nombre                          AS rutina,
        r.objetivo,
        r.fecha_creacion,
        COUNT(ej.id_ejercicio)            AS total_ejercicios
    FROM rutina r
    JOIN cliente    c  ON c.cedula  = r.cedula_cliente
    JOIN persona    pc ON pc.cedula = c.cedula
    JOIN entrenador e  ON e.cedula  = r.cedula_entrenador
    JOIN persona    pe ON pe.cedula = e.cedula
    LEFT JOIN ejercicio ej ON ej.id_rutina = r.id_rutina
    GROUP BY r.id_rutina, pc.nombre, pe.nombre, r.nombre, r.objetivo, r.fecha_creacion
    ORDER BY r.fecha_creacion DESC;

-- =============================================================================
-- VISTA 10: Usuarios activos del sistema con su rol
-- Uso: módulo de autenticación y administración
-- =============================================================================
CREATE OR REPLACE VIEW v_usuarios_activos AS
    SELECT
        u.id_usuario,
        u.username,
        u.rol,
        u.nombre,
        u.creado_en,
        u.cedula_entrenador,
        e.especialidad
    FROM usuario u
    LEFT JOIN entrenador e ON e.cedula = u.cedula_entrenador
    WHERE u.activo = TRUE
    ORDER BY u.rol, u.nombre;
