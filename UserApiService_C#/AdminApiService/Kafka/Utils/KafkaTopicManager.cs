﻿using Confluent.Kafka;
using Confluent.Kafka.Admin;
using KafkaTestLib.KafkaException;

namespace KafkaTestLib.Kafka;

public class KafkaTopicManager : IDisposable, IAsyncDisposable
{
    private AdminClientConfig _config;
    private IAdminClient _adminClient;
    public KafkaTopicManager()
    {
        _config = new AdminClientConfig() { BootstrapServers = Environment.GetEnvironmentVariable("KAFKA_BROKERS")};
        _adminClient = new AdminClientBuilder(_config).Build();
    }

    public async Task<bool> CheckTopicExists(string topicName)
    {
        try
        {
            var topicExists = _adminClient.GetMetadata(topicName, TimeSpan.FromSeconds(10));
            if (topicExists.Topics.Count == 0)
            {
                return false;
            }
            return true;
        }
        catch (Exception e)
        {
            
            Console.WriteLine($"An error occurred: {e.Message}"); 
            throw new CheckTopicException("Failed to check topic");
        }
    }

    public async Task<bool> CreateTopic(string topicName, int numPartitions, short replicationFactor)
    {
        try
        {
       
            var result = _adminClient.CreateTopicsAsync(new TopicSpecification[] 
            { 
                new TopicSpecification { 
                    Name = topicName,
                    NumPartitions = numPartitions, 
                    ReplicationFactor =  replicationFactor,   
                    Configs = new Dictionary<string, string>
                    {
                        { "min.insync.replicas", "2" } 
                    }} 
            });
            if (result.IsCompleted)
            {
                return true;
            }
            throw new CreateTopicException("Failed to create topic");
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            throw new CreateTopicException("Failed to create topic");
        }
    }

    public void Dispose()
    {
        _adminClient.Dispose();
    }

    public async ValueTask DisposeAsync()
    {
        _adminClient.Dispose();
    }
}