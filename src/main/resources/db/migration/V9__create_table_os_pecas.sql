CREATE TABLE os_pecas
(
    id                       BIGSERIAL      NOT NULL,
    ordem_servico_id         BIGINT         NOT NULL,
    peca_id                  BIGINT         NOT NULL,
    quantidade               INTEGER        NOT NULL,
    preco_unitario_historico NUMERIC(10, 2) NOT NULL,
    data_registro            TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_os_pecas PRIMARY KEY (id),
    CONSTRAINT fk_os_pecas_ordem_servico FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico (id),
    CONSTRAINT fk_os_pecas_peca FOREIGN KEY (peca_id) REFERENCES pecas (id)
);
