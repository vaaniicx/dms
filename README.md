# Document Management System

Group A

## Quick Start

```sh
./run.sh args... #  or  .\run.ps1 args... 
```

### Arguments
Both files support the following arguments:

```sh
       # default (no argument) → same as "start"
start  # build & compose up
stop   # compose down all running containers
dev    # build & compose up all non-local images (PG, NGINX, ...)
```

2.Access
- [Adminer](http://localhost:9090)

## Project Structure

- `dms`
    - `docker` Infrastructure
    - `docs` Requirements & Documentation
    - `rest` REST API
    - `tests` Integration tests


## Project Architecture
### Spring REST API
- JDK: `temurin-22`
- SpringBoot 3.5.5
- Swagger Annotations (OpenAPI) 2.2.12

![Diagram](https://www.plantuml.com/plantuml/dpng/TP1HJ_em5CNV-obEzWx_mHyY6490yS7466JovaktNcV3rfAsGnFZTzUkAvaXFjvpxkdvzQwiuyOrTSKgUdLm6aQMRmv26YgSr8enfGKXsUZYG0QtkZvgIyWSBmu9FXbGU4cHSaWM-J8x6gbAxEwoPwotzuj_-V95NS_IMOe4tCAGTGmGtF6SMsepMsrTQIXxUi1esYMca__ZDY3oYuwrgAKYW-bmqctFqtdQRlDWiOlCZdZXC9fZtp5Pnvqia3Uo4e7CRrpa7akfFZ0QZg_YLqAHyhgl-GbfWsH3fu1Bfzzlt1ZzRib4CmyZOmn-n62mxCle3QqNFuJGwBLumxuGqBqHNip14BDdvOknZdOVYHfV-WVj6RM9C57f0uP3h4Y9fgw-0G00)
