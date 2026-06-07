-- =============================================================================
-- FITCLUB — 01_schema.sql
-- Definición de tipos ENUM y creación de tablas
-- Propuestas implementadas: ENUMs, Autenticación, Máquinas, Ejercicios, Turnos
-- =============================================================================

-- =============================================================================
-- SECCIÓN 1 — ENUMs
-- =============================================================================

CREATE TYPE tipo_membresia   AS ENUM ('mensual', 'trimestral', 'semestral', 'anual');
CREATE TYPE estado_membresia AS ENUM ('activa', 'vencida', 'cancelada');
CREATE TYPE metodo_pago      AS ENUM ('efectivo', 'tarjeta', 'transferencia');
CREATE TYPE tipo_asistencia  AS ENUM ('entrada', 'salida');
CREATE TYPE estado_maquina   AS ENUM ('disponible', 'en_uso', 'mantenimiento');
CREATE TYPE rol_usuario      AS ENUM ('admin', 'recepcionista', 'entrenador');
CREATE TYPE dia_semana       AS ENUM ('lunes','martes','miercoles','jueves','viernes','sabado','domingo');

-- =============================================================================
-- SECCIÓN 2 — TABLAS BASE (sin dependencias)
-- =============================================================================

-- Tabla persona: base para cliente y entrenador
CREATE TABLE persona (
    cedula    VARCHAR(20)  PRIMARY KEY,
    nombre    VARCHAR(100) NOT NULL,
    telefono  VARCHAR(20)  NOT NULL
);

-- Tabla cliente
CREATE TABLE cliente (
    cedula           VARCHAR(20)  PRIMARY KEY REFERENCES persona(cedula) ON DELETE CASCADE,
    fecha_nacimiento DATE,
    direccion        VARCHAR(200) NOT NULL,
    email            VARCHAR(150) UNIQUE,
    fecha_registro   DATE         DEFAULT CURRENT_DATE,
    activo           BOOLEAN      DEFAULT TRUE,
    CONSTRAINT chk_edad_minima
        CHECK (fecha_nacimiento IS NULL
            OR fecha_nacimiento <= CURRENT_DATE - INTERVAL '14 years')
);

-- Tabla entrenador
CREATE TABLE entrenador (
    cedula       VARCHAR(20)  PRIMARY KEY REFERENCES persona(cedula) ON DELETE CASCADE,
    especialidad VARCHAR(150) NOT NULL,
    activo       BOOLEAN      DEFAULT TRUE
);

-- Catálogo de tipos de membresía
CREATE TABLE membresia (
    id_membresia  SERIAL         PRIMARY KEY,
    tipo          tipo_membresia  NOT NULL,
    costo         NUMERIC(10,2)   NOT NULL,
    beneficios    TEXT,
    duracion_dias INT             NOT NULL,
    activa        BOOLEAN         DEFAULT TRUE,
    CONSTRAINT chk_costo_positivo    CHECK (costo > 0),
    CONSTRAINT chk_duracion_positiva CHECK (duracion_dias > 0)
);

-- Tabla máquinas del gimnasio
CREATE TABLE maquina (
    id_maquina SERIAL         PRIMARY KEY,
    nombre     VARCHAR(150)   NOT NULL,
    tipo       VARCHAR(100),
    ubicacion  VARCHAR(100),
    estado     estado_maquina  DEFAULT 'disponible',
    activa     BOOLEAN         DEFAULT TRUE
);

-- =============================================================================
-- SECCIÓN 3 — TABLAS CON DEPENDENCIAS
-- =============================================================================

-- Membresía asignada a un cliente
CREATE TABLE cliente_membresia (
    id           SERIAL          PRIMARY KEY,
    cedula       VARCHAR(20)     NOT NULL REFERENCES cliente(cedula),
    id_membresia INT             NOT NULL REFERENCES membresia(id_membresia),
    fecha_inicio DATE            NOT NULL,
    fecha_fin    DATE            NOT NULL,
    estado       estado_membresia DEFAULT 'activa',
    CONSTRAINT chk_fechas_membresia CHECK (fecha_fin > fecha_inicio)
);

-- Asignación de entrenador a cliente
CREATE TABLE cliente_entrenador (
    id                SERIAL      PRIMARY KEY,
    cedula_cliente    VARCHAR(20) NOT NULL REFERENCES cliente(cedula)    ON DELETE CASCADE,
    cedula_entrenador VARCHAR(20) NOT NULL REFERENCES entrenador(cedula) ON DELETE CASCADE,
    fecha_asignacion  DATE        DEFAULT CURRENT_DATE,
    UNIQUE (cedula_cliente, cedula_entrenador)
);

-- Turnos semanales del entrenador
CREATE TABLE turno_entrenador (
    id_turno          SERIAL      PRIMARY KEY,
    cedula_entrenador VARCHAR(20) NOT NULL REFERENCES entrenador(cedula) ON DELETE CASCADE,
    dia               dia_semana  NOT NULL,
    hora_inicio       TIME        NOT NULL,
    hora_fin          TIME        NOT NULL,
    CONSTRAINT chk_turno_valido CHECK (hora_fin > hora_inicio),
    UNIQUE (cedula_entrenador, dia)
);

-- Rutinas asignadas a clientes
CREATE TABLE rutina (
    id_rutina         SERIAL       PRIMARY KEY,
    cedula_cliente    VARCHAR(20)  NOT NULL REFERENCES cliente(cedula),
    cedula_entrenador VARCHAR(20)  NOT NULL REFERENCES entrenador(cedula),
    nombre            VARCHAR(150) NOT NULL,
    objetivo          VARCHAR(255),
    descripcion       TEXT,
    fecha_creacion    DATE         DEFAULT CURRENT_DATE
);

-- Ejercicios dentro de una rutina
CREATE TABLE ejercicio (
    id_ejercicio SERIAL       PRIMARY KEY,
    id_rutina    INT          NOT NULL REFERENCES rutina(id_rutina) ON DELETE CASCADE,
    nombre       VARCHAR(150) NOT NULL,
    series       INT          CHECK (series > 0),
    repeticiones INT          CHECK (repeticiones > 0),
    descripcion  TEXT,
    id_maquina   INT          REFERENCES maquina(id_maquina)
);

-- Pagos de clientes
CREATE TABLE pago (
    id_pago     SERIAL        PRIMARY KEY,
    cedula      VARCHAR(20)   NOT NULL REFERENCES cliente(cedula),
    monto       NUMERIC(10,2) NOT NULL,
    fecha_pago  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    metodo_pago metodo_pago   NOT NULL,
    concepto    VARCHAR(200),
    CONSTRAINT chk_monto_positivo CHECK (monto > 0)
);

-- Asistencia de clientes con tipo entrada/salida
CREATE TABLE asistencia (
    id_asistencia SERIAL          PRIMARY KEY,
    cedula        VARCHAR(20)     NOT NULL REFERENCES cliente(cedula),
    fecha_hora    TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    tipo          tipo_asistencia NOT NULL,
    observacion   TEXT
);

-- Uso de máquinas por clientes
CREATE TABLE uso_maquina (
    id_uso            SERIAL      PRIMARY KEY,
    id_maquina        INT         NOT NULL REFERENCES maquina(id_maquina),
    cedula            VARCHAR(20) NOT NULL REFERENCES cliente(cedula),
    fecha_hora_inicio TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_hora_fin    TIMESTAMP,
    CONSTRAINT chk_uso_fechas
        CHECK (fecha_hora_fin IS NULL OR fecha_hora_fin > fecha_hora_inicio)
);

-- Progreso físico de clientes (IMC calculado por trigger)
CREATE TABLE progreso (
    id_progreso   SERIAL      PRIMARY KEY,
    cedula        VARCHAR(20) NOT NULL REFERENCES cliente(cedula),
    fecha         DATE        DEFAULT CURRENT_DATE,
    peso_kg       NUMERIC(5,2) NOT NULL,
    altura_m      NUMERIC(4,2) NOT NULL,
    imc           NUMERIC(5,2),
    observaciones TEXT,
    CONSTRAINT chk_peso_valido   CHECK (peso_kg  BETWEEN 20  AND 500),
    CONSTRAINT chk_altura_valida CHECK (altura_m BETWEEN 0.5 AND 2.5)
);

-- Usuarios del sistema con control de acceso por rol
CREATE TABLE usuario (
    id_usuario        SERIAL      PRIMARY KEY,
    username          VARCHAR(50) UNIQUE NOT NULL,
    password          VARCHAR(100) NOT NULL,
    CONSTRAINT chk_password_longitud CHECK (LENGTH(password) >= 6),
    rol               rol_usuario NOT NULL DEFAULT 'recepcionista',
    nombre            VARCHAR(100),
    activo            BOOLEAN     DEFAULT TRUE,
    creado_en         TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    cedula_entrenador VARCHAR(20) REFERENCES entrenador(cedula)
);
