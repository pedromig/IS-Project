CREATE TABLE stations(
    id BIGSERIAL NOT NULL,
    name VARCHAR(512),
    PRIMARY KEY (name)
);

CREATE TABLE locations (
    id BIGSERIAL NOT NULL,
    name VARCHAR(512),
    PRIMARY KEY (name)
);
