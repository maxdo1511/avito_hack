// config.go
package config

import (
	"encoding/json"
	"os"
)

type Config struct {
	KafkaServer    string `json:"kafka_server"`
	KafkaPort      int    `json:"kafka_port"`
	KafkaTopic     string `json:"kafka_topic"`
	KafkaGroup     string `json:"kafka_group"`
	KafkaOffset    string `json:"kafka_offset"`
	KafkaPartition int32  `json:"kafka_partition"`
}

func Read(configFile string) (Config, error) {
	var config Config
	file, err := os.Open(configFile)
	if err != nil {
		return config, err
	}
	defer file.Close()
	decoder := json.NewDecoder(file)
	err = decoder.Decode(&config)
	if err != nil {
		return config, err
	}
	return config, nil
}
