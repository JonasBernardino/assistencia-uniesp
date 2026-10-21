CREATE TABLE usuarios
(
    id           BIGSERIAL    NOT NULL,
    nome         VARCHAR(150) NOT NULL,
    email        VARCHAR(150) NOT NULL,
    senha        VARCHAR(255) NOT NULL,
    perfil       VARCHAR(20)  NOT NULL,
    ativo        BOOLEAN      NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_usuarios PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_email UNIQUE (email)
);
