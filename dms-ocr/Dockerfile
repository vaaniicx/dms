## build stage
FROM maven:3.9-eclipse-temurin-22 AS builder
WORKDIR /workspace

# copy entire multi-module project
COPY . .

# build only this service + its dependencies
RUN mvn -pl dms-ocr -am clean package -DskipTests

## runtime stage
FROM eclipse-temurin:22-jre
WORKDIR /app

# Install OCR runtime
RUN apt-get update && apt-get install -y --no-install-recommends \
	tesseract-ocr \
	libtesseract-dev \
	tesseract-ocr-all \
	&& rm -rf /var/lib/apt/lists/*

# tessdata path used by tess4j
ENV TESSDATA_PREFIX=/usr/share/tesseract-ocr/5/tessdata

# copy jar from builder stage
COPY --from=builder /workspace/dms-ocr/target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
