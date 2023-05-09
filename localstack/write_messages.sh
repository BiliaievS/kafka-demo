docker exec --interactive --tty broker \
kafka-console-producer --bootstrap-server broker:9092 \
                       --topic demo_topic

# How to https://developer.confluent.io/quickstart/kafka-docker/:
#Type in some lines of text. Each line is a new message.
#
#this is my first kafka message
#hello world!
#this is my third kafka message. I’m on a roll :-D
