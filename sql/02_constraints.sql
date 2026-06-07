-- =============================================================================
-- FITCLUB — 02_constraints.sql
-- Restricciones adicionales de integridad referencial y de negocio
-- =============================================================================

-- Validar que un cliente no tenga dos membresías activas simultáneas
-- (se gestiona vía stored procedure, pero se agrega índice parcial como respaldo)
CREATE UNIQUE INDEX uq_cliente_membresia_activa
    ON cliente_membresia (cedula)
    WHERE estado = 'activa';

-- Validar que un usuario con rol 'entrenador' tenga cedula_entrenador asignada
ALTER TABLE usuario
    ADD CONSTRAINT chk_usuario_entrenador
    CHECK (
        rol <> 'entrenador'
        OR cedula_entrenador IS NOT NULL
    );

-- Validar que la fecha de fin de uso de máquina sea posterior al inicio
-- (ya incluida en el schema como CHECK, se documenta aquí para referencia)

-- Validar que un ejercicio no tenga 0 series ni 0 repeticiones
-- (ya incluida en el schema como CHECK > 0)

-- Regla: un entrenador solo puede tener un turno por día de la semana
-- (ya garantizada con UNIQUE (cedula_entrenador, dia) en turno_entrenador)

-- Regla: un cliente no puede estar asignado dos veces al mismo entrenador
-- (ya garantizada con UNIQUE (cedula_cliente, cedula_entrenador) en cliente_entrenador)

-- Constraint adicional: la fecha de registro del cliente no puede ser futura
ALTER TABLE cliente
    ADD CONSTRAINT chk_fecha_registro_valida
    CHECK (fecha_registro <= CURRENT_DATE);

-- Constraint adicional: el nombre de usuario no puede contener espacios
ALTER TABLE usuario
    ADD CONSTRAINT chk_username_sin_espacios
    CHECK (username NOT LIKE '% %');

-- Constraint: la contraseña debe tener al menos 6 caracteres
-- (ya incluido como CHECK en el schema, se documenta aquí para referencia)
