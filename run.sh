#!/bin/bash
set -euo pipefail

cp .env.example .env

ACTION="${1:-start}"

docker compose down -v --remove-orphans
if [ "$ACTION" == "stop" ]; then
    exit 0
fi

if [ "$ACTION" == "dev" ]; then
    docker compose up db adminer -d
    exit 0
fi

docker compose up -d