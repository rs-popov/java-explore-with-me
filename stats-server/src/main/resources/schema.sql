DROP TABLE IF EXISTS statistics;

CREATE TABLE IF NOT EXISTS statistics
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    uri        VARCHAR(255),
    attributes JSONB,
    created    TIMESTAMP,
    CONSTRAINT pk_statistics PRIMARY KEY (id)
);