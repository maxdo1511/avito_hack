using Newtonsoft.Json;

namespace UserApiService.Models.RequestModels;

public class GetInfoByName : IRequest
{
    [JsonProperty("location")]
    public string location { get; set; }
    [JsonProperty("category")]
    public string category { get; set; }
}