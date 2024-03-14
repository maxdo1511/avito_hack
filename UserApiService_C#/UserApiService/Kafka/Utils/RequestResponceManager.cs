using Confluent.Kafka;
using UserApiService.Models.ReplyModels;
using UserApiService.Models.RequestModels;

namespace KafkaTestLib.Kafka;

public class RequestResponseManager : IDisposable
{
    private IKafkaConsumer _consumer;
    private IKafkaProducer _producer;

    public RequestResponseManager(IKafkaConsumer consumer, IKafkaProducer producer)
    {
        _consumer = consumer;
        _producer = producer;
    }

    public async Task<IReply> GetReply(Message<Guid, IRequest> message)
    {
        await _producer.Produce(message);
        return await _consumer.ConsumeReply(message.Key);
    }
    public void Dispose()
    {
        _consumer.Dispose();
        _producer.Dispose();
    }
}