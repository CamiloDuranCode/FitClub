
CREATE TABLE persona (
    cedula      VARCHAR(20)  NOT NULL,
    nombre      VARCHAR(100) NOT NULL,
    telefono    VARCHAR(20),
    email       VARCHAR(100),
    CONSTRAINT pk_persona PRIMARY KEY (cedula)
);


CREATE TABLE entrenador (
    cedula       VARCHAR(20)  NOT NULL,
    especialidad VARCHAR(100),
    horario      VARCHAR(100),
    CONSTRAINT pk_entrenador         PRIMARY KEY (cedula),
    CONSTRAINT fk_entrenador_persona FOREIGN KEY (cedula)
        REFERENCES persona(cedula)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE cliente (
    cedula            VARCHAR(20)  NOT NULL,
    fecha_nacimiento  DATE,
    direccion         VARCHAR(200),
    entrenador_cedula VARCHAR(20),
    CONSTRAINT pk_cliente            PRIMARY KEY (cedula),
    CONSTRAINT fk_cliente_persona    FOREIGN KEY (cedula)
        REFERENCES persona(cedula)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_cliente_entrenador FOREIGN KEY (entrenador_cedula)
        REFERENCES entrenador(cedula)
        ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TYPE tipo_membresia AS ENUM ('mensual', 'trimestral', 'anual');
CREATE TYPE estado_membresia AS ENUM ('activa', 'vencida', 'suspendida');

CREATE TABLE membresia (
    id_membresia      SERIAL       NOT NULL,
    cliente_cedula    VARCHAR(20)  NOT NULL,
    tipo              tipo_membresia   NOT NULL,
    fecha_inicio      DATE         NOT NULL,
    fecha_vencimiento DATE         NOT NULL,
    estado            estado_membresia NOT NULL DEFAULT 'activa',
    CONSTRAINT pk_membresia        PRIMARY KEY (id_membresia),
    CONSTRAINT fk_membresia_cliente FOREIGN KEY (cliente_cedula)
        REFERENCES cliente(cedula)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TYPE metodo_pago_enum AS ENUM ('efectivo', 'tarjeta', 'transferencia');

CREATE TABLE pago (
    id_pago      SERIAL          NOT NULL,
    membresia_id INTEGER         NOT NULL,
    monto        NUMERIC(10,2)   NOT NULL,
    fecha_pago   DATE            NOT NULL,
    metodo_pago  metodo_pago_enum NOT NULL,
    CONSTRAINT pk_pago           PRIMARY KEY (id_pago),
    CONSTRAINT fk_pago_membresia FOREIGN KEY (membresia_id)
        REFERENCES membresia(id_membresia)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE asistencia (
    id_asistencia  SERIAL       NOT NULL,
    cliente_cedula VARCHAR(20)  NOT NULL,
    fecha_hora     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacion    VARCHAR(255),
    CONSTRAINT pk_asistencia        PRIMARY KEY (id_asistencia),
    CONSTRAINT fk_asistencia_cliente FOREIGN KEY (cliente_cedula)
        REFERENCES cliente(cedula)
        ON UPDATE CASCADE ON DELETE RESTRICT
);


CREATE TABLE rutina (
    id_rutina         SERIAL       NOT NULL,
    cliente_cedula    VARCHAR(20)  NOT NULL,
    entrenador_cedula VARCHAR(20)  NOT NULL,
    descripcion       TEXT,
    fecha_asignacion  DATE         NOT NULL,
    CONSTRAINT pk_rutina             PRIMARY KEY (id_rutina),
    CONSTRAINT fk_rutina_cliente     FOREIGN KEY (cliente_cedula)
        REFERENCES cliente(cedula)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_rutina_entrenador  FOREIGN KEY (entrenador_cedula)
        REFERENCES entrenador(cedula)
        ON UPDATE CASCADE ON DELETE RESTRICT
);


CREATE TABLE progreso (
    id_progreso    SERIAL        NOT NULL,
    cliente_cedula VARCHAR(20)   NOT NULL,
    rutina_id      INTEGER       NOT NULL,
    fecha_registro DATE          NOT NULL,
    peso           NUMERIC(5,2),
    talla          NUMERIC(4,2),
    observaciones  TEXT,
    CONSTRAINT pk_progreso         PRIMARY KEY (id_progreso),
    CONSTRAINT fk_progreso_cliente FOREIGN KEY (cliente_cedula)
        REFERENCES cliente(cedula)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_progreso_rutina  FOREIGN KEY (rutina_id)
        REFERENCES rutina(id_rutina)
        ON UPDATE CASCADE ON DELETE RESTRICT
);