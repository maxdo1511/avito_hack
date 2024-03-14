using Newtonsoft.Json;

namespace UserApiService.Models.RequestModels;

public class GetInfoById : IRequest
{
    [JsonProperty("locationId")]
    public int locationId { get; set; }
    [JsonProperty("categoryId")]
    public int categoryId { get; set; }
}