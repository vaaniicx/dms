param (
    [string]$action = "start"
)

$ErrorActionPreference = "Stop"

Copy-Item .env.example .env -Force

if ($action -eq "stop") {
    $running = docker compose ps --status=running | Select-String "Up"

    if ($running) {
        docker compose down -v --remove-orphans
    }

    exit
}

docker compose build
docker compose up -d