# Document Management System

## Quick Start

1. Create the environment file.

```sh
cp .env.example docker/.env
```

2. Build services.

```sh
cd docker
docker compose build
```

3. Run the stack.

```sh
docker compose up -d
```

4. Access
- [Adminer](http://localhost:8081)

## Project Structure

- `dms`
    - `docker` Infrastructure
    - `docs` Requirements & Documentation
    - `rest` REST API
    - `tests` Integration tests


## Project Architecture

![Diagram](https:////www.plantuml.com/plantuml/png/RP2zJiCm58LtFyLH9XYgLln8LLHL3Iq35KCW89OkTdm9LflhoBvCgE-EKneGQZsUS_pfqrr4qK4-svtQqRj0F89hFWJM1Qh5ULQAlILaArzlAM3fsyv7GYMwtcRuKWhuDmWKfS-HNjsFcLYGpSoyweBZ1i_trGkAyc4sDlCDRxZ_a4ydqqa67Q4xfu3z2TQQ0MgrQACZ7PYbZz84gfuU1vmr-UtDzTK1PirPbhQjOmhdBOkV4g-_WiVvd-Cn6DX96QCHh46EzUfkYJ7MPQ6GafGFwrGcIroyaKdiIxLN2sAR3lyD)
