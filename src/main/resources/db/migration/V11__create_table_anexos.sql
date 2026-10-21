CREATE TABLE anexos
(
    id                   BIGSERIAL    NOT NULL,
    ordem_servico_id     BIGINT       NOT NULL,
    nome_original        VARCHAR(255) NOT NULL,
    nome_arquivo_storage VARCHAR(255) NOT NULL,
    tipo_conteudo        VARCHAR(100) NOT NULL,
    tamanho_bytes        BIGINT       NOT NULL,
    caminho_storage      VARCHAR(500) NOT NULL,
    data_upload          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_anexos PRIMARY KEY (id),
    CONSTRAINT uk_anexos_storage UNIQUE (nome_arquivo_storage),
    CONSTRAINT fk_anexos_ordem_servico FOREIGN KEY (ordem_servico_id) REFERENCES ordens_servico (id)
);
