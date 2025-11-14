## build stage
FROM maven:3.9-eclipse-temurin-22 AS builder
WORKDIR /workspace

# cache dependencies
COPY pom.xml .

# copy sources and build
COPY src ./src
RUN mvn clean package

## runtime stage
FROM eclipse-temurin:22-jre
WORKDIR /app

# copy jar from builder stage
COPY --from=builder /workspace/target/*-SNAPSHOT.jar app.jar

EXPOSE 4040
ENTRYPOINT ["java","-jar","/app/app.jar"]