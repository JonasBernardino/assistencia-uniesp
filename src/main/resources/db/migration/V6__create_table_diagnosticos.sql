CREATE TABLE diagnosticos
(
    id                BIGSERIAL NOT NULL,
    ordem_servico_id  BIGINT    NOT NULL,
    tecnico_id        BIGINT    NOT NULL,
    descricao_tecnica TEXT      NOT NULL,
    solucao_proposta  TEXT      NOT NULL,
    data_registro     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_diagnosticos PRIMARY KEY (id),
    CONSTRAINT uk_diagnosticos_ordem_servico UNIQUE (ordem_servico_id),
    CONSTRAINT fk_diagnosticos_ordem_servico FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico (id),
    CONSTRAINT fk_diagnosticos_tecnico FOREIGN KEY (tecnico_id) REFERENCES tecnicos (id)
);
