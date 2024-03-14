using Newtonsoft.Json;

namespace AdminApiService.Models.Requests;

public class UserAskPriceRequest
{
    [JsonProperty("userCategoryIds")]
    public List<long> userCategoryIds { get; set; }
    [JsonProperty("categoryId")]
    public long categoryId { get; set; }
    [JsonProperty("locationId")]
    public long locationId { get; set; }
    [JsonProperty("date")]
    public long date { get; set; } 
}