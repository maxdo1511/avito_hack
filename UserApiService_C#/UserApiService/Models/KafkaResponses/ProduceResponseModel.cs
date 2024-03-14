using Confluent.Kafka;

namespace KafkaTestLib.Models;

public class ProduceResponseModel : DefaultResponseModel
{
    public bool Success { get; set; }
    public string ErrorMessage { get; set; }

}