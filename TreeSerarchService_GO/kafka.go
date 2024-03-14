package main

import (
	"fmt"
	"github.com/IBM/sarama"
)

func StartKafkaConsumer(kafkaConnection string, kafkaTopic string, kafkaGroup string, kafkaOffset string, kafkaPartition int32) {
	consumer, err := sarama.NewConsumer([]string{kafkaConnection}, nil)
	if err != nil {
		panic(err)
	}
	defer func() {
		if err := consumer.Close(); err != nil {
			panic(err)
		}
	}()

	partitionConsumer, err := consumer.ConsumePartition(kafkaTopic, kafkaPartition, sarama.OffsetOldest)
	if err != nil {
		panic(err)
	}
	defer func() {
		if err := partitionConsumer.Close(); err != nil {
			panic(err)
		}
	}()

	for {
		select {
		case msg := <-partitionConsumer.Messages():
			fmt.Printf("Received message: %s\n", msg.Value)
		}
	}
}
