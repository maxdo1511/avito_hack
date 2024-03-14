using AdminApiService.Models.Replies;
using KafkaTestLib.Models;

namespace KafkaTestLib.Kafka;

public interface IKafkaConsumer : IDisposable
{
    public Task<DefaultResponseModel> IsTopicAvailable();
    public Task Consume();
    public Task<IReply> ConsumeReply(string messageId);
    public void Dispose();
}