using KafkaTestLib.Models;
using UserApiService.Models.ReplyModels;

namespace KafkaTestLib.Kafka;

public interface IKafkaConsumer : IDisposable
{
    public Task<DefaultResponseModel> IsTopicAvailable();
    public Task Consume();
    public Task<IReply> ConsumeReply(Guid messageId);
    public void Dispose();
}