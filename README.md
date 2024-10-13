# EcommerceFlink

This project uses **Apache Flink** to process real-time product streams from **Kafka** and index them into **Elasticsearch**.

## Features
- Stream processing with **Flink**.
- Data ingestion from **Kafka** (`myteck_products` topic).
- Indexing products into **Elasticsearch** (`products_v3` index).

## Prerequisites
- **Java 11**
- **Kafka** running on `localhost:29092`
- **Elasticsearch** running on `localhost:9200`

