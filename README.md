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

## Project Structure

- `dms`
    - `docker` Infrastructure
    - `docs` Requirements & Documentation
    - `rest` REST API


## Project Architecture

![Diagram](https://www.plantuml.com/plantuml/png/SoWkIImgAStDuKhEpot8pqlDAr5Giaco2oueoinBLmXo3GvHS0pmjD5Fiel9YydBoKzEpCd8BowniZ2mC5HII2nMI2p8v4f9B4bCIYnE1MiD04gd5wK61_fWwZ8rCJO2wqf9uk82wbJGrRK3iXMi5BnSd9mLz1My0Xov75BpKe0k0W00)
