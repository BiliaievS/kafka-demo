#!/usr/bin/env bash

docker run -d --name demo-zookeeper-server -p 2181:2181 --network kafka-demo -e ZOOKEEPER_CLIENT_PORT=2181 -e ZOOKEEPER_TICK_TIME=2000 confluentinc/cp-zookeeper:latest

# docker run -d \ -e ALLOW_ANONYMOUS_LOGIN=yes
# --net=host \
# --name=zookeeper \
# -e ZOOKEEPER_CLIENT_PORT=32181 \
# -e ZOOKEEPER_TICK_TIME=2000 \
# -e ZOOKEEPER_SYNC_LIMIT=2 \
# confluentinc/cp-zookeeper:5.4.0
# $ docker network create app-tier
# $ docker run -p 5000:2181 -e ALLOW_ANONYMOUS_LOGIN=yes --network app-tier --name zookeeper-server  bitnami/zookeeper:latest
# $ docker run -p 9092:9092 -e ALLOW_PLAINTEXT_LISTENER=yes -e KAFKA_ZOOKEEPER_CONNECT=zookeeper-server:2181 --network app-tier --name kafka-server bitnami/kafka:latest