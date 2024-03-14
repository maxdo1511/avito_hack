using Newtonsoft.Json;

namespace AdminApiService.Models.Requests;

public class GetStatisticAboutUserCategoryInLocationInPeriodRequest : IRequest
{
    [JsonProperty("userCategoryIds")]
    public List<long> userCategoryIds { get; set; }
    [JsonProperty("locationId")]
    public long locationId { get; set; }
    [JsonProperty("startDate")]
    public long startDate { get; set; }
    [JsonProperty("endDate")]
    public long endDate { get; set; }
}