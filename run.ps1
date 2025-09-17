param (
    [string]$action = "start"
)

$ErrorActionPreference = "Stop"

Copy-Item .env.example .env -Force

docker compose down -v --remove-orphans

if ($action -eq "stop") {
    exit
}

if ($action -eq "rest-dev") {
    docker compose up db adminer -d
    exit
}

docker compose up -d