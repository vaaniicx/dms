$ErrorActionPreference = "Stop"

Set-Location docker

Copy-Item ../.env.example .env -Force

# Check if any containers are running
$running = docker compose ps --status=running | Select-String "Up"

if ($running) {
    docker compose down -v --remove-orphans
}

docker compose build
docker compose up -d

Set-Location ..