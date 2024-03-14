using Confluent.Kafka;
using KafkaTestLib.Kafka;
using KafkaTestLib.KafkaException.ConsumerException;
using Microsoft.AspNetCore.Mvc;
using UserApiService.Models.ReplyModels;
using UserApiService.Models.RequestModels;

namespace UserApiService.Controllers;

[Route("/info")]
public class InfoController : ControllerBase
{
    [HttpPost]
    [Route("withIds")]
    public async Task<IActionResult> GetInfoWithIds()
    {
        IReply reply = new ReplyWithIds();
        using (KafkaRequestWithIdsProducer producer = new KafkaRequestWithIdsProducer("infoWithIdsRequests", Acks.All, CompressionType.None))
        {
            using (KafkaReplyWithIdsConsumer consumer = new KafkaReplyWithIdsConsumer("infoWithIdsReplies",true,1000,true, AutoOffsetReset.Latest, "infoWithIds"))
            {
                using (RequestResponseManager manager = new RequestResponseManager(consumer, producer))
                {
                   reply = await manager.GetReply(new Message<Guid, IRequest>(){Key = Guid.NewGuid(), Value = new GetInfoById(){locationId = 1, categoryId = 1}});
                }
            }
        }

        if (reply != null)
        {
            return Ok(reply);
        }

        throw new ConsumerRecievedMessageInvalidException();
    }
}