CREATE DATABASE asdb WITH
    OWNER = postgres
    ENCODING ='UTF8'
    TABLESPACE =pg_default
    CONNECTION LIMIT =-1;
GRANT ALL ON DATABASE asdb TO postgres;
