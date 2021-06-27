#!/usr/bin/env bash

docker run -d \
	--name demo-kafka \
	-p 9092:9092 \
	--network kafka-demo \
	-e KAFKA_BROKER_ID=1 \
	-e KAFKA_ZOOKEEPER_CONNECT="demo-zookeeper-server:2181" \
	-e KAFKA_ADVERTISED_LISTENERS="PLAINTEXT://localhost:9092" \
	-e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
	confluentinc/cp-kafka:5.4.0

# docker run -d \
#     --net=host \
#     --name=kafka \
#     -e KAFKA_ZOOKEEPER_CONNECT=localhost:32181 \
#     -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:29092 \
#     -e KAFKA_BROKER_ID=2 \
#     -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
#     confluentinc/cp-kafka:5.4.0