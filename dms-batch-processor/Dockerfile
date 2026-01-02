## build stage
FROM maven:3.9-eclipse-temurin-22 AS builder
WORKDIR /workspace

# copy entire multi-module project
COPY . .

# build only this service + its dependencies
RUN mvn -pl dms-batch-processor -am clean package -DskipTests

## runtime stage
FROM eclipse-temurin:22-jre
WORKDIR /app

# copy the built jar from the correct module
COPY --from=builder /workspace/dms-batch-processor/target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]

