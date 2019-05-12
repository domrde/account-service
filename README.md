# Account service

## Description
Account service with two logic endpoints:

- `GET /account/amount/{id}` - Retrieve current value of account with specified `id`.
- `POST /account/amount/{id}?value={value}` - Increase account's with `id` value by `value`.

Request processing statistic is available on endpoints:

- `GET /metric/retrieve/{timeWindow}/minutes` - Chart of retrieval requests per second for `timeWindow` minutes.
- `GET /metric/update/{timeWindow}/minutes` - Chart of update requests per second for `timeWindow` minutes.

## Running the server

Server can be started as a jar file after `gradle build` or using `gradle server:bootRun`. Following services should be installed for it to work: 
- PostgreSQL DB
- Zookeeper + Apache Kafka
- Netflix Atlas

Database should have database `asdb` with `account` table defined as:
```sql
CREATE TABLE account
(
    id      INTEGER UNIQUE,
    value   BIGINT,
    version BIGINT
);
``` 

## Running the client

Client can be started as a jar file after `gradle build` or using `gradle client:bootRun`.

## Docker

Server with all of its dependencies can be started using `docker compose up` executed in `docker` folder.

Client can be started using Dockerfile in `docker/client`.

Following environment variables should be provided to client's docker image:
- `R_COUNT` - readers number.
- `W_COUNT` - writers number.
- `ID_LIST` - list of allowed ids.
- `HOST` - server's host.
       
Example command to start client:
```bash
docker build -t as-client docker/client
docker run -e R_COUNT=4 -e W_COUNT=4 -e ID_LIST='1000-2000' -e HOST='192.168.1.100' as-client
```

Mind following:
- Docker containers and especially client's one have worse performance.
- Kafka image handles restarts poorly and can fail with error, so only container with server should be restarted if needed. Or `docker-compose up --force-recreate --build` can be used.
