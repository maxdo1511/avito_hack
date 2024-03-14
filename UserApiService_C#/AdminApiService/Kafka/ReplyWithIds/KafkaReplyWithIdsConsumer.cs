using System.Diagnostics;
using System.Runtime.InteropServices.JavaScript;
using System.Threading.RateLimiting;
using AdminApiService.Models.Replies;
using AdminApiService.Models.Requests;
using Confluent.Kafka;
using Confluent.Kafka.SyncOverAsync;
using Confluent.SchemaRegistry.Serdes;
using KafkaTestLib.KafkaException;
using KafkaTestLib.KafkaException.ConsumerException;
using KafkaTestLib.Models;

namespace KafkaTestLib.Kafka;

public class KafkaReplyWithIdsConsumer : IKafkaConsumer
{
    private string _topicName;
    private ConsumerConfig _config;
    private IConsumer<string, IReply> _consumer; 
    private Offset _offset;
    
    public KafkaReplyWithIdsConsumer(string topicName, bool EnableAutoCommit, int AutoCommitIntervalMs, bool EnableAutoOffsetStore, AutoOffsetReset autoOffsetReset, string groupId)
    {
        _topicName = topicName;
        _config = new ConsumerConfig() {BootstrapServers = Environment.GetEnvironmentVariable("KAFKA_BROKERS"),GroupId = groupId, EnableAutoCommit =EnableAutoCommit, AutoCommitIntervalMs = AutoCommitIntervalMs, EnableAutoOffsetStore = EnableAutoOffsetStore, AutoOffsetReset = autoOffsetReset};

        _consumer = new ConsumerBuilder<string, IReply>(_config)
            .SetValueDeserializer(new JsonDeserializer<IReply>().AsSyncOverAsync()).Build();
        bool isTopicAvailable = IsTopicAvailable().Result.Success;
        
        if(isTopicAvailable)
        {
            _consumer.Subscribe(_topicName);
        }
    }

    public async Task<DefaultResponseModel> IsTopicAvailable()
    {
        try
        {
            using (KafkaTopicManager manager = new KafkaTopicManager())
            {
                bool IsTopicExists = await manager.CheckTopicExists(_topicName);
                if (IsTopicExists)
                {
                    return new DefaultResponseModel() { Success = true };
                }

                throw new ConsumerTopicUnavailableException("Topic unavailable");
            }
        }
        catch (Exception e)
        {
            if (!(e is MyKafkaException))
            {
                Console.WriteLine("Error checking topic" + e);
                throw new ConsumerException(e.ToString());
            }
            Console.WriteLine(e);
            throw e;
        }
    }
    public async Task Consume()
    {
        try
        {
            while (true)
            {
                ConsumeResult<string, IReply> result = _consumer.Consume(5000);

                if (result != null)
                {
                    switch (result.Message)
                    {
                        
                        default:
                            throw new ConsumerRecievedMessageInvalidException("Invalid message received");
                    }

                }
            }
        }
        catch(Exception ex)
        {
            if (!(ex is MyKafkaException))
            {
                throw new ConsumerException(ex.ToString());
            }
            else
            {
                throw ex;
            }
        }
    }

   

    public async Task<IReply> ConsumeReply(string MessageKey)
    {
        try
        {
            while (true)
            {
                ConsumeResult<string, IReply> result = _consumer.Consume(5000);

                if (result != null)
                {
                    if (result.Message.Key==MessageKey)
                    {
                        return result.Message.Value;
                    }

                }
            }
        }
        catch(Exception ex)
        {
            if (!(ex is MyKafkaException))
            {
                throw new ConsumerException(ex.ToString());
            }
            else
            {
                throw ex;
            }
        }
    }
    public void Dispose()
    {
        _consumer.Dispose();
    }
}