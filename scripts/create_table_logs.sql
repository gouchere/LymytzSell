-- public.yvs_logs_factures definition

-- Drop table

-- DROP TABLE public.yvs_logs_factures;

CREATE TABLE public.yvs_logs_factures
(
    id        bigserial NOT NULL,
    entete    bigint      NOT NULL,
    date_doc  date      NOT NULL,
    vendeur   bigint      NOT NULL,
    creneau   bigint      NOT NULL,
    content_json jsonb     NULL,
    CONSTRAINT yvs_logs_factures_pk PRIMARY KEY (id)
);
CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE INDEX idx_numero_externe_trgm ON yvs_com_doc_ventes USING gin (numero_externe gin_trgm_ops);
CREATE INDEX idx_num_doc_trgm ON yvs_com_doc_ventes USING gin (num_doc gin_trgm_ops);