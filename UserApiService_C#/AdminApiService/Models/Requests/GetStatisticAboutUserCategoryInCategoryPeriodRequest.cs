using Newtonsoft.Json;

namespace AdminApiService.Models.Requests;

public class GetStatisticAboutUserCategoryInCategoryPeriodRequest : IRequest
{
    [JsonProperty("userCategoryIds")]
    public List<long> userCategoryIds { get; set; }
    [JsonProperty("categoryId")]
    public long categoryId { get; set; }
    [JsonProperty("dateStart")]
    public long dateStart { get; set; }
    [JsonProperty("dateEnd")]
    public long dateEnd { get; set; }

}