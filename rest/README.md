# Setup Project
## Setup Database
### Prerequisites
- Docker installed

### Run PostgreSQL with Docker
Run the following command to create and start a PostgreSQL container with a preconfigured dms database:
```bash
docker run --name postgres-dms -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=dms -p 5432:5432 -d postgres:15
```

### Verify Database
To check that the database is running:
```bash
docker exec -it postgres-dms psql -U postgres -c "\l"
```