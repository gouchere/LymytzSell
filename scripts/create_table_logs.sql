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