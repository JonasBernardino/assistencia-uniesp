CREATE TABLE orcamentos
(
    id                    BIGSERIAL      NOT NULL,
    ordem_servico_id      BIGINT         NOT NULL,
    valor_mao_de_obra     NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    valor_pecas           NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    valor_total           NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    data_criacao          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_expiracao        TIMESTAMP      NOT NULL,
    status                VARCHAR(20)    NOT NULL DEFAULT 'PENDENTE',
    data_resposta         TIMESTAMP,
    justificativa_cliente TEXT,

    CONSTRAINT pk_orcamentos PRIMARY KEY (id),
    CONSTRAINT uk_orcamentos_ordem_servico UNIQUE (ordem_servico_id),
    CONSTRAINT fk_orcamentos_ordem_servico FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico (id)
);
