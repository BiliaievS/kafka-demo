docker exec --interactive --tty broker \
kafka-console-consumer --bootstrap-server broker:9092 \
                       --topic demo_topic \
                       --from-beginning

# https://developer.confluent.io/quickstart/kafka-docker/
