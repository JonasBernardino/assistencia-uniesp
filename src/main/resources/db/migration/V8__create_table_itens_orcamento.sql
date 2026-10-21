CREATE TABLE itens_orcamento
(
    id               BIGSERIAL      NOT NULL,
    orcamento_id     BIGINT         NOT NULL,
    peca_id          BIGINT         NOT NULL,
    quantidade       INTEGER        NOT NULL,
    preco_unitario   NUMERIC(10, 2) NOT NULL,
    valor_total_item NUMERIC(10, 2) NOT NULL,

    CONSTRAINT pk_itens_orcamento PRIMARY KEY (id),
    CONSTRAINT fk_itens_orcamento_orcamento FOREIGN KEY (orcamento_id) REFERENCES orcamentos (id),
    CONSTRAINT fk_itens_orcamento_peca FOREIGN KEY (peca_id) REFERENCES pecas (id)
);
