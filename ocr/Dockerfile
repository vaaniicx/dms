## build stage
FROM maven:3.9-eclipse-temurin-22 AS builder
WORKDIR /workspace

# First, build and install the ocr module itself
COPY ocr/pom.xml ./ocr/pom.xml
RUN cd ocr && mvn dependency:go-offline

# copy sources and build
COPY ocr/src ./ocr/src
RUN cd ocr && mvn clean package -DskipTests

## runtime stage
FROM eclipse-temurin:22-jre
WORKDIR /app

# install tesseract and ghostscript
RUN apt-get update \
    && apt-get install -y --no-install-recommends tesseract-ocr ghostscript \
    && rm -rf /var/lib/apt/lists/*

# copy jar from builder stage
COPY --from=builder /workspace/ocr/target/*-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
