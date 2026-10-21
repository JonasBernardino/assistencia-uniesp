CREATE TABLE pecas
(
    id                 BIGSERIAL     NOT NULL,
    nome               VARCHAR(150)  NOT NULL,
    codigo             VARCHAR(50)   NOT NULL,
    preco              NUMERIC(10, 2) NOT NULL,
    quantidade_estoque INTEGER       NOT NULL DEFAULT 0,
    ativo              BOOLEAN       NOT NULL DEFAULT TRUE,

    CONSTRAINT pk_pecas PRIMARY KEY (id),
    CONSTRAINT uk_pecas_codigo UNIQUE (codigo)
);
