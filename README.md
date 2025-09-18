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

![Diagram](https://www.plantuml.com/plantuml/png/ROtBIiH044NtzHMNx4SHdCqmX8aFK70HJS1rFyhOJD8Tgoku4FzTJJ08pggRKuTp3rcDw3JqrFEBGXCaTAyA7uITXXI9OlACuYQv8mkkqp2cp6ZKk6E13mBS3ueS5SNUbiqe8NPukcrQLFNz_joM1Ko6S5O2l55ZJURPhLFMJhXvVC35TZMz_HUj_10Y2xPdbwjc5aQ4kOtDfeIts65Sglcj9_nQ9w-VHCPf4XHL7npFrIcpq9_nPxQualJ-ZUUxNVv4KrwgArdUcQshEzorRKq7ZduQ-Yy0)
