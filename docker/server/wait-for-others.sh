#!/bin/bash

set -e

until psql -U "postgres" -c '\q'; do
  >&2 echo "Postgres is unavailable - sleeping"
  sleep 5
done

>&2 echo "Postgres is up, waiting for Kafka"

until [[ $(echo exit | busybox-extras telnet localhost 9092) != "*Connected to*" ]]; do
  >&2 echo "Kafka is unavailable - sleeping"
  sleep 5
done

sleep 5

>&2 echo "Kafka is up - executing command"

exec "$@"
