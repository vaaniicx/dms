## build stage
FROM maven:3.9-eclipse-temurin-22 AS builder
WORKDIR /workspace

# copy entire multi-module project
COPY . .

# build only this service + its dependencies
RUN mvn -pl dms-rest -am clean package -DskipTests

## runtime stage
FROM eclipse-temurin:22-jre
WORKDIR /app

# copy jar from builder stage
COPY --from=builder /workspace/dms-rest/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
