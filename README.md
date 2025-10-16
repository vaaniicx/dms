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

![Diagram](https://www.plantuml.com/plantuml/dpng/TL9TJy8m57tlhxZPExWX21X2GFJ1H1IOlFdITdKsi1Qx3asC_zsMkvgPIRAakH_dtdFEtQoqrDYgs9EzUBLWDHYPlrd8f17SIgq8KJeJ4DrglXC65heuw590QYdVHV150BXFAQ4iH1CsJGz6gXmsT-app5VtamisVL7NetWKGmIq12DAwq0PjPHHDyXZLhgqkH7f-W6IGOqOz2zx9q36MZlcMI6LC33ezklkSJoEJzqKIwI5sGZABIHZsDQSd70hER1AxZDavZjQkCTSgaz8khrXa8ABbC__iuOo9kro3VsWushOSq_YtWG5cUBTuzo7pYubDyvISvHSb6Zf6LEV5XkSUfgZkI40ttwM8wwDkv4uecNY-9VOZT4VRaCeCWo9W_eGEfrfCmsDmDSyOsXbWzMkWcPWAvwtcK1NVG4NCMY6tWtmMXfHc8pjn4ci1Mw3YChpF2FmLrqLc8PCaptURoPKvdx-7m00)
