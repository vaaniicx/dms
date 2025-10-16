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

![Diagram](https://www.plantuml.com/plantuml/dpng/TL9TJy8m57tlhpZPExWX21X2GFJ1n0zWykPBkzNPm5hiEZGn_dVNTTKPv6cvvvxJUywzcnM6QbFcExBZBmP6GOlirI0LcYT6A4c8L2b7SAsIBTTOg7ol2exOq6GRu9C0YOKabmR1X4tZlHOom_ecVi9yUJivOzDdUJaAHo42q0AQ5uO0AJMKqOBNcgKgJAPvlBf3D6WKW_vvhnQax6Z6FCs5v1g3RhzXZyDnMBEfQv4ZpPhJn20QOzDuge8RaO2LOfSwPvPnf7l8XFn0rEqDVHGtI5hydp6KCM7kL-Zx71V3NlM9gc_a0kdyZPnQs-DYxNGrKoLraCgMqjK5-C4jX1_-og-qUY-uzatcIcAbLObEPzg6qIhgdbyPN2QB-Ikr0RSSst0lChHB6mhMy6_b7uQZaoz2cYGRxxQqXaua-ObHUgCUQPjtPvXncLR_y3S0)
