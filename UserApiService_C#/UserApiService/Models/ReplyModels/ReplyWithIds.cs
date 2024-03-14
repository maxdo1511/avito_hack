using Newtonsoft.Json;

namespace UserApiService.Models.ReplyModels;

public class ReplyWithIds : IReply
{
    [JsonProperty("price")]
    public int Price { get; set; }
    [JsonProperty("location_id")]
    public int LocationId { get; set; }
    [JsonProperty("microcategory_id")]
    public int MicroCategoryId { get; set; }
    [JsonProperty("matrix_id")]
    public int MatrixId { get; set; }
    [JsonProperty("user_segment_id")]
    public int UserSegmentId { get; set; }
}