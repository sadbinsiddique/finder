CREATE TABLE employee
(
    id        INT AUTO_INCREMENT NOT NULL,
    fast_name VARCHAR(255)       NULL,
    last_name VARCHAR(255)       NULL,
    email     VARCHAR(255)       NULL,
    CONSTRAINT pk_employee PRIMARY KEY (id)
);