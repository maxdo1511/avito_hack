using Confluent.Kafka;
using KafkaTestLib.Models;
using UserApiService.Models.ReplyModels;
using UserApiService.Models.RequestModels;

namespace KafkaTestLib.Kafka;

public interface IKafkaProducer : IDisposable
{
    void Dispose();
    Task<ProduceResponseModel> Produce(Message<Guid, IRequest> message);
}