CREATE USER webuser WITH PASSWORD 'password';
CREATE DATABASE metallica_db;
GRANT ALL PRIVILEGES ON DATABASE metallica_db TO webuser;

\connect metallica_db webuser;

CREATE TABLE metallica
(
  id          SERIAL PRIMARY KEY,
  name        VARCHAR(50),
  instrument  VARCHAR(50),
  entrydate   DATE,
  networth    INT,
  birthdate   DATE,
  binfield    BYTEA
);

INSERT INTO metallica( name, instrument, entrydate, networth, birthdate, binfield )
VALUES( 'James Hetfield', 'Guitar', '1981.10.28', 300000000, '1963.08.03', '\xDEADBEEF' );

INSERT INTO metallica( name, instrument, entrydate, networth, birthdate, binfield)
VALUES( 'Lars Ulrich', 'Drums', '1981.10.28', 300000000, '1963.12.26', '\xBABADEAF');

INSERT INTO metallica( name, instrument, entrydate, networth, birthdate, binfield)
VALUES( 'Kirk Hammet', 'Guitar', '1983.04.01', 200000000, '1962.11.18', '\xDEAFDADD');

INSERT INTO metallica( name, instrument, entrydate, networth, birthdate, binfield)
VALUES( 'Robert Trujillo', 'Bass', '1983.04.01', 20000000, '1964.10.23', '\xBEEFBABA');
