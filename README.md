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

![Diagram](https://www.plantuml.com/plantuml/dpng/TL9DZzem5BpxLunoXoQK5AWGWj8UAlK3IBdrOYVUh0MnMSTXLrhjVr_7ZbTPPJbPRzvCtinxMTU6QjDKPtBcTmP6GOlotg0GckT6A4c8L0L7z5tb9wwnLTL5rHoPeVafmZC1yXQIN1f4IxRABbh84eSVsJziTZ-NdzZgl_mwZ-Sn8j0QcjU600KrbD6QEyrErQRKFDl_Gf9sYdJwUU841HlCs1ILa5mZ7KyxzY9Un8vDVOiCD0TESuDaWMFdoGOt8WThnBdmpbh6GFUd5F89oNWo2r7y8DdY7uoXZ0dpUu_WBFqoczWdiM_Y0-dgWTnQsz_jmUiSK-KkYBMbjBq0l_g5yEjx-JUj7ckkGvFvahHIMs8qMlLXz0hNitumGCCYGU2ZEj0Vhm_udRc67yJGArrMj3bwLVYofvCNGhg0nn2YQNtU2FYMhWbE0z9txw-pvhAml_Oh)
