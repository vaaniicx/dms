$ErrorActionPreference = "Stop"

Copy-Item .env.example .env -Force

$running = docker compose ps --status=running | Select-String "Up"

if ($running) {
    docker compose down -v --remove-orphans
}

docker compose build
docker compose up -d