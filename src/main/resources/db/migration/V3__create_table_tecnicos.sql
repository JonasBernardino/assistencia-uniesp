CREATE TABLE tecnicos
(
    id            BIGSERIAL    NOT NULL,
    nome          VARCHAR(150) NOT NULL,
    email         VARCHAR(150) NOT NULL,
    telefone      VARCHAR(20),
    especialidade VARCHAR(100),
    ativo         BOOLEAN      NOT NULL DEFAULT TRUE,

    CONSTRAINT pk_tecnicos PRIMARY KEY (id),
    CONSTRAINT uk_tecnicos_email UNIQUE (email)
);
