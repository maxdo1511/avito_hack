using AdminApiService.Models.Requests;
using Confluent.Kafka;
using KafkaTestLib.Models;

namespace KafkaTestLib.Kafka;

public interface IKafkaProducer : IDisposable
{
    void Dispose();
    Task<ProduceResponseModel> Produce(Message<string, IRequest> message);
}