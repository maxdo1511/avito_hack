using System.Runtime.InteropServices;
using Confluent.Kafka;
using Confluent.Kafka.SyncOverAsync;
using Confluent.SchemaRegistry;
using Confluent.SchemaRegistry.Serdes;
using KafkaTestLib.KafkaException;
using KafkaTestLib.Models;
using UserApiService.Models.ReplyModels;
using UserApiService.Models.RequestModels;

namespace KafkaTestLib.Kafka;

public class KafkaRequestWithIdsProducer : IKafkaProducer
{
    private string _topicName;
    private ProducerConfig _config;
    private IProducer<Guid, GetInfoById> _producer;
    public KafkaRequestWithIdsProducer(string topicName, Acks requiredAcs, CompressionType compressionType)
    {
        _topicName = topicName;
        _config = new ProducerConfig() { BootstrapServers = Environment.GetEnvironmentVariable("KAFKA_BROKERS"), Partitioner = Partitioner.Murmur2 ,Acks = requiredAcs, CompressionType = compressionType };
        _producer = new ProducerBuilder<Guid, GetInfoById>(_config).SetValueSerializer(new JsonSerializer<GetInfoById>( new CachedSchemaRegistryClient(new SchemaRegistryConfig
        {
            
            Url = Environment.GetEnvironmentVariable("SCHEMA_REGISTRY_URL")
        })).AsSyncOverAsync()).Build();
    }
    public KafkaRequestWithIdsProducer(string topicName, [Optional]int BatchNumMessages,  [Optional]int Linger, Acks requiredAcs, CompressionType compressionType)
    {
        _topicName = topicName;
        _config = new ProducerConfig() { BootstrapServers = Environment.GetEnvironmentVariable("KAFKA_BROKERS"), Partitioner = Partitioner.Murmur2, LingerMs = Linger,Acks = requiredAcs, BatchNumMessages = BatchNumMessages, CompressionType = compressionType };
        _producer = new ProducerBuilder<Guid, GetInfoById>(_config).SetValueSerializer(new JsonSerializer<GetInfoById>( new CachedSchemaRegistryClient(new SchemaRegistryConfig
        {
            
            Url = Environment.GetEnvironmentVariable("SCHEMA_REGISTRY_URL")
        })).AsSyncOverAsync()).Build();
    }
   
    
    public async Task<ProduceResponseModel> Produce(Message<Guid, IRequest> message)
    {
        try
        {
            using (var topicManager = new KafkaTopicManager())
            {
        
                bool IsTopicExists = await topicManager.CheckTopicExists(_topicName);
                if (IsTopicExists)
                {
                    var deliveryResult = await _producer.ProduceAsync(_topicName, new Message<Guid, GetInfoById>() { Key = message.Key, Value = (GetInfoById)message.Value });
                    if (deliveryResult.Status == PersistenceStatus.Persisted)
                    {
        
                        Console.WriteLine("Message delivery status: Persisted");
                        return new ProduceResponseModel() { Success = true , ErrorMessage = ""};
                    }
                    else
                    {
      
                        throw new MessageProduceException("Message delivery status: Not persisted");
                    }
                }
                else
                {
                    bool IsTopicCreated = await topicManager.CreateTopic(_topicName, Convert.ToInt32(Environment.GetEnvironmentVariable("PARTITIONS_STANDART")), Convert.ToInt16(Environment.GetEnvironmentVariable("REPLICATION_FACTOR_STANDART")));
                    if (IsTopicCreated)
                    {
                        var deliveryResult = await _producer.ProduceAsync(_topicName, new Message<Guid, GetInfoById>() { Key = message.Key, Value = (GetInfoById)message.Value });
                        if (deliveryResult.Status == PersistenceStatus.Persisted)
                        {
                            Console.WriteLine("Message delivery status: Persisted");
                            return new ProduceResponseModel() { Success = true , ErrorMessage = ""};
                        }
                        else
                        {
                            throw new MessageProduceException("Message delivery status: Not persisted");
                        }
                    }
                    return new ProduceResponseModel();
                }
        
          

            }
        }
        catch (Exception e)
        {
            if (!(e is MyKafkaException))
            {
                throw new ProducerException(e.ToString());
            }
            throw e;
        }
       
       
        
    }


 

    public void Dispose()
    {
        _producer.Dispose();
    }
}