CREATE TABLE historico_status
(
    id               BIGSERIAL    NOT NULL,
    ordem_servico_id BIGINT       NOT NULL,
    status_anterior  VARCHAR(30),
    novo_status      VARCHAR(30)  NOT NULL,
    data_alteracao   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    responsavel      VARCHAR(150) NOT NULL,
    observacao       VARCHAR(500),

    CONSTRAINT pk_historico_status PRIMARY KEY (id),
    CONSTRAINT fk_historico_status_ordem_servico FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico (id)
);
