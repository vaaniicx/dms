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
- [Adminer](http://localhost:9090)

## Project Structure

- `dms`
    - `docker` Infrastructure
    - `docs` Requirements & Documentation
    - `rest` REST API
    - `tests` Integration tests


## Project Architecture
### Spring REST API
- JDK: `temurin-22`.
- SpringBoot 3.5.5
- Swagger Annotations (OpenAPI) 2.2.12

![Diagram](https://www.plantuml.com/plantuml/png/RP5DQm8n48RlyojUShVLLa4M4Uq7AFImxGez9vkf3Qw9JKPwAFtltOqhRS7RpZmpppjB5abqbEE-eORV118GtVPTO5taMbpmH3vOXde8zOuZxiF-41AZ4btl5Bw8WDD0v2J5T3WTAkY4hcef0DJTYcSlK3CphmxH-IrU7giDbkLwTctcYe3lNv4rnMHyUvCrrlGIgqKRdJWxof1a6xbwVi9eSi6WxTvvZjqrewpf6nSFMlPiSZN-fyf_C1ZCkniA74wHwQCXd7KNQRkhQmaHQjcucdFyCN6aFwclR70NmsVRpjoSledQmMmzTr10hTQbehxEBATdjjfiIbgmj-tx_G00)
