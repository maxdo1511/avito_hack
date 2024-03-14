package main

import (
	"fmt"
	"go_tree_service/config"
	"go_tree_service/tree_search"
)

func main() {
	cfg, _ := ReadConfig()
	tree_search.InitTrees()
	StartKafka(cfg)
}

func ReadConfig() (config.Config, error) {
	configFile := "config.json"
	cfg, err := config.Read(configFile)
	if err != nil {
		fmt.Println(err)
		return cfg, err
	}
	return cfg, nil
}

func StartKafka(config config.Config) {
	StartKafkaConsumer(config.KafkaServer+":"+fmt.Sprint(config.KafkaPort),
		config.KafkaTopic,
		config.KafkaGroup,
		config.KafkaOffset,
		config.KafkaPartition)
}
