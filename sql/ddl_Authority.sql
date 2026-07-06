CREATE TABLE authorities
(
    id        BIGINT      NOT NULL,
    username  VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT pk_authorities PRIMARY KEY (id)
);

ALTER TABLE authorities
    ADD CONSTRAINT FK_AUTHORITIES_ON_USERNAME FOREIGN KEY (username) REFERENCES users (username);