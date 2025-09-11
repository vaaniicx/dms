#!/bin/bash
set -euo pipefail

cd docker

if [ ! -f .env ]; then
    cp ../.env.example .env
fi

if docker compose ps --status=running | grep -q 'Up'; then
    docker compose down -v --remove-orphans
fi

docker compose build

docker compose up -d

cd ..
