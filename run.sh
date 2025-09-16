#!/bin/bash
set -euo pipefail

cp .env.example .env

if docker compose ps --status=running | grep -q 'Up'; then
    docker compose down -v --remove-orphans
fi

docker compose build
docker compose up -d