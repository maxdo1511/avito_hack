using Newtonsoft.Json;

namespace AdminApiService.Models.Requests;

public class GetStatisticAboutCategoryInLocationInPeriodRequest
{
    [JsonProperty("categoryId")]
    public long categoryId { get; set; }
    [JsonProperty("locationId")]
    public long locationId { get; set; }
    [JsonProperty("dateStart")]
    public long dateStart { get; set; }
    [JsonProperty("dateEnd")]
    public long dateEnd { get; set; }
}