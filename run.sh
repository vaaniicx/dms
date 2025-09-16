#!/bin/bash
set -euo pipefail

cp .env.example .env

ACTION="${1:-start}"

if [ "$ACTION" == "stop" ]; then
    if docker compose ps --status=running | grep -q 'Up'; then
        docker compose down -v --remove-orphans
    fi
    exit 0
fi

docker compose build
docker compose up -d
