-- Drop tables if they exist to prevent errors on restart (optional, but helpful during dev)
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS users;

-- The Users Table
CREATE TABLE users
(
    username VARCHAR(50) NOT NULL PRIMARY KEY,
    password VARCHAR(68) NOT NULL,
    enabled  TINYINT(1)  NOT NULL
);

-- The Authorities Table (Roles)
CREATE TABLE authorities
(
    username  VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users (username)
);

-- An index to ensure a user doesn't get assigned the exact same role twice
CREATE UNIQUE INDEX ix_auth_username ON authorities (username, authority);

-- Insert login accounts
INSERT INTO users (username, password, enabled)
VALUES ('alice', '{noop}1234', 1),
       ('bob', '{noop}1234', 1),
       ('charlie', '{noop}1234', 1),
       ('diana', '{noop}1234', 1);


-- Alice is a standard employee (Can only GET)
INSERT INTO authorities (username, authority)
VALUES ('alice', 'ROLE_EMPLOYEE');

-- Bob is a manager (Can GET, POST, PUT, PATCH)
INSERT INTO authorities (username, authority)
VALUES ('bob', 'ROLE_EMPLOYEE'),
       ('bob', 'ROLE_MANAGER');

-- Charlie is an admin/manager (Can GET, POST, PUT, PATCH)
INSERT INTO authorities (username, authority)
VALUES ('charlie', 'ROLE_EMPLOYEE'),
       ('charlie', 'ROLE_MANAGER'),
       ('charlie', 'ROLE_ADMIN');

-- Diana currently has no roles assigned (Will likely get a 403 Forbidden)
-- This is great for testing what happens when a user logs in but has no permissions!
