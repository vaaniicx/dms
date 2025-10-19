## build stage
FROM maven:3.9-eclipse-temurin-22 AS builder
WORKDIR /workspace

# First, build and install the ocr module
COPY ocr/pom.xml ./ocr/pom.xml
COPY ocr/src ./ocr/src
RUN cd ocr && mvn clean install -DskipTests

# Now build the rest module
COPY rest/pom.xml ./rest/pom.xml
RUN cd rest && mvn dependency:go-offline

# copy rest sources and build
COPY rest/src ./rest/src
RUN cd rest && mvn clean package

## runtime stage
FROM eclipse-temurin:22-jre
WORKDIR /app

# copy jar from builder stage
COPY --from=builder /workspace/rest/target/*-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
