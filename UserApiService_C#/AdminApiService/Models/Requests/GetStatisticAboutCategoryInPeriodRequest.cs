using Newtonsoft.Json;

namespace AdminApiService.Models.Requests;

public class GetStatisticAboutCategoryInPeriodRequest
{
    [JsonProperty("categoryId")]
    public long categoryId { get; set; }
    [JsonProperty("dateStart")]
    public long dateStart { get; set; }
    [JsonProperty("dateEnd")]
    public long dateEnd { get; set; } 
}