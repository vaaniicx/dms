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
- [Web UI](http://localhost:80)
- [MinIO](http://localhost:9001)
- [RabbbitMQ](http://localhost:15672)
- [Adminer](http://localhost:9090)

## Project Structure

- `dms`
  - `docs` Requirements & Documentation
  - `rest` Spring REST API
  - `ocr`  Spring OCR Scanner
  - `ui`   React Web UI

## Project Architecture
### Spring REST API
- JDK: `temurin-22`
- SpringBoot 3.5.5
- Swagger Annotations (OpenAPI) 2.2.12

![Diagram](https://www.plantuml.com/plantuml/dpng/TLF1Rjim3BthAmZVtSbUlTH34RJhTYYmh6sytdQHPCqJ4akPB6S3XltxQDCA77Wv2IJV8_aUxRik21_wTi_s-dk0uC2RvaU0sdYjWd6MCUjg3Sb7ftRQmuDh3wxJK0MXTWdyOG1gI5bj0oItSbqTlB4DR3zLN-7--V7sZLn_i-zl-0s714G7NdS1IRK8GefEZvndruN6w-hbCsJ5n2Ykt-OZePQB6VTrQwpsK5nTJ-YIbtn42ogn1MUhXGgGbV0wxOJ2Wr4WUxElfyqGiU1zQOpz3TbLlefIQ90TylyP8wGqOVC2SRFYtIh79yCt8q6-_Ocq1NDZxECJv3mlKbwcV7LInZazsSX6USOnQa2NsHZbyIJ-Q9Jkw2GNZlBqiAMkOoUdF4B_3hrGkFDuOtqao_NnVBqFtlt22R4eAMG35KD8q_Ky3RDWhA5YM6JZgR12PXAKFezvGgZp33a08vH6IuZXD2s6SpZAdPLZ5Ajn6Uh7q9N9azUekn-aNV3aP4WVnSNCN31zCdVQrlY__GC0)