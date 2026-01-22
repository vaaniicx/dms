# Document Management System (DMS)

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
- [Elasticsearch](http://localhost:9200)
- [Kibana](http://localhost:5601)

## Project Structure

- `dms`
  - `docs` Requirements & Documentation
  - `rest` Spring REST API
  - `ocr` Spring OCR Scanner
  - `ui` React Web UI

## Project Architecture

```mermaid
%%{init: { 'themeVariables': { 'fontSize':'16px', 'fontFamily':'monospace' } }}%%
graph TD
    classDef serviceStyle fill:#66D9EF22,stroke:#66D9EF,stroke-width:2px,color:#F8F8F2
    classDef uiStyle fill:#A6E22E22,stroke:#A6E22E,stroke-width:2px,color:#F8F8F2
    classDef dataStyle fill:#FD971F22,stroke:#FD971F,stroke-width:2px,color:#F8F8F2
    classDef brokerStyle fill:#F9267222,stroke:#F92672,stroke-width:2px,color:#F8F8F2
    classDef searchStyle fill:#AE81FF22,stroke:#AE81FF,stroke-width:2px,color:#F8F8F2
    classDef toolStyle fill:#E6DB7422,stroke:#E6DB74,stroke-width:2px,color:#F8F8F2
    classDef volumeStyle fill:#75715E33,stroke:#75715E,stroke-width:2px,color:#F8F8F2
    classDef actorStyle fill:#272822,stroke:#66D9EF,stroke-width:2px,color:#F8F8F2
    classDef subgraphStyle fill:#272822,stroke:#75715E,stroke-width:1px,color:#F8F8F2

    rest["Spring REST API<br/>:8080"]
    ocr["Spring OCR<br/>worker"]
    batch["Batch Processor<br/>worker"]
    genai["Gen-AI Service<br/>:4040"]

    ui["Nginx 1.27 + React 19<br/>:80"]

    db[(PostgreSQL 15<br/>:5432)]
    rmq["RabbitMQ 4 (Mgmt)<br/>:5672 / :15672"]
    minio[(MinIO 2025-09-07<br/>:9000 / :9001)]
    adminer["Adminer 5.4<br/>:9090"]
    es["Elasticsearch 8.15<br/>:9200 / :9300"]
    kibana["Kibana 8.15<br/>:5601"]
    ollama["Ollama<br/>:11434"]

    access_logs[(access-logs)]
    ollama_data[(ollama-data)]


    browser -->|HTTP| ui
    devbrowser -->|HTTP| adminer
    devbrowser -->|HTTP| rmq
    devbrowser -->|HTTP| minio
    devbrowser -->|HTTP| kibana
    devbrowser -->|HTTP| es
    devbrowser -->|HTTP| ollama

    ui -->|Proxy /api| rest
    adminer -->|JDBC| db
    kibana -->|HTTP| es

    rest -->|JDBC| db
    rest -->|AMQP| rmq
    rest -->|S3 API| minio
    rest -->|HTTP| es

    ocr -->|AMQP| rmq
    ocr -->|S3 API| minio
    ocr -->|HTTP| es

    batch -->|JDBC| db
    batch -->|AMQP| rmq
    batch --- access_logs

    genai -->|AMQP| rmq

    ollama --- ollama_data

    class rest,ocr,batch,genai serviceStyle
    class ui uiStyle
    class db,minio dataStyle
    class rmq brokerStyle
    class es searchStyle
    class adminer,kibana,ollama toolStyle
    class access_logs,ollama_data volumeStyle
    class user,dev,browser,devbrowser actorStyle
```

### Spring REST API

- JDK: `temurin-22`
- SpringBoot 3.5.5
- Swagger Annotations (OpenAPI) 2.2.12

### Messaging

```mermaid
%%{init: { 'themeVariables': { 'fontSize':'16px', 'fontFamily':'monospace' } }}%%
graph LR
    classDef serviceStyle fill:#66D9EF22,stroke:#66D9EF,stroke-width:3px,color:#F8F8F2
    classDef queueStyle fill:#A6E22E22,stroke:#A6E22E,stroke-width:3px,color:#F8F8F2
    classDef exchangeStyle fill:#F9267222,stroke:#F92672,stroke-width:3px,color:#F8F8F2
    classDef servicesSubgraphStyle fill:#66D9EF22,stroke:#66D9EF,stroke-width:2px,color:#F8F8F2
    classDef queuesSubgraphStyle fill:#A6E22E22,stroke:#A6E22E,stroke-width:2px,color:#F8F8F2
    classDef messagingSubgraphStyle fill:#F9267222,stroke:#F92672,stroke-width:2px,color:#F8F8F2

    subgraph services[Services]
        rest[dms-rest]
        ocr[dms-ocr]
        genai[dms-gen-ai]
        batch[dms-batch-processor]
    end

    subgraph messaging[Message Broker]
        exchange["document.exchange<br/>(Direct Exchange)"]
    end

    subgraph queues[Queues]
        q_ocr_upload[ocr.document.uploaded.queue]
        q_rest_scan[rest.document.scanned.queue]
        q_genai_scan[genai.document.scanned.queue]
        q_rest_index[rest.document.indexed.queue]
        q_rest_summary[rest.document.summarized.queue]
        q_rest_access[rest.document.accessed.queue]
    end

    rest -->|"1. Publish<br/>[document.uploaded]"| exchange
    exchange -->|"2. Route<br/>[document.uploaded]"| q_ocr_upload
    q_ocr_upload -->|"3. Consume"| ocr

    ocr -->|"4a. Publish<br/>[document.scanned]<br/><br/>4b. Publish<br/>[document.indexed]"| exchange

    exchange -->|"5a. Route<br/>[document.scanned]"| q_rest_scan
    exchange -->|"5b. Route<br/>[document.scanned]"| q_genai_scan
    exchange -->|"5c. Route<br/>[document.indexed]"| q_rest_index

    q_rest_scan -->|"6a. Consume<br/>(Update: SCANNED)"| rest
    q_genai_scan -->|"6b. Consume"| genai
    q_rest_index -->|"6c. Consume<br/>(Update: INDEXED)"| rest

    genai -->|"7. Publish<br/>[document.summarized]"| exchange
    exchange -->|"8. Route<br/>[document.summarized]"| q_rest_summary
    q_rest_summary -->|"9. Consume<br/>(Update: SUMMARIZED)"| rest

    batch -->|"Publish<br/>[document.accessed]"| exchange
    exchange -->|"Route<br/>[document.accessed]"| q_rest_access
    q_rest_access -->|"Consume<br/>(Update access history)"| rest

    class rest,ocr,genai,batch serviceStyle
    class q_ocr_upload,q_rest_scan,q_genai_scan,q_rest_index,q_rest_summary,q_rest_access queueStyle
    class exchange exchangeStyle
    class services servicesSubgraphStyle
    class queues queuesSubgraphStyle
    class messaging messagingSubgraphStyle
```
