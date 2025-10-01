#!/bin/bash
set -euo pipefail

cp .env.example .env

ACTION="${1:-start}"

if [ "$ACTION" == "stop" ]; then
    docker compose down -v --remove-orphans
    exit 0
fi

if [ "$ACTION" == "dev" ]; then
    docker compose up db rmq adminer -d
    exit 0
fi

if [ "$ACTION" == "rest" ]; then
    cd rest
    mvn spring-boot:run
    cd -
    exit 0
fi

docker compose build --no-cache ui
docker compose up -d