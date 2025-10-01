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

### Access
- [Adminer DB UI](http://localhost:9090)
- [RabbitMQ UI](http://localhost:9093)

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
- RabbitMQ 3.13


![Diagram](https://www.plantuml.com/plantuml/dpng/TL9DRy8m5BldLrYzmrfAnuSGWdqSaCO4TDou96tMHT24fIcRDEs_ho4jOggu83txEROVdHM6QbFc1tBWxmP6GOlimo0LcYT6A4c8L2c7zwIIFTTuLFbH5HoneSdUmmy14Wj9BWsy2Pl6HoraXirp_8RvQZ6vOzET70_zeGyFj83cXQb4AJMKqOAVDIjLc4ppUFs28AeLKJ_idGKfQtdCqrn8hX5r-_Nso1_vvstgADBIR3XD388HjdMcghWH2LWf3gbBPZTQkjTCo6y4tTwWgUACfCNRBfGnOPPhXDqWT8BvSht2EBeVX3kvNS163PrV_cbZLgPAL_Vb9rUNd_w_-g3LLy5rGp8t4YkjHdGwqsiOLuFbceEq2MiF-skd-cznhMu1xZoMSAuC1Ctx5-INaBhHjabTsc0t6btgb4sk1hdEweuzupAjljG_)
