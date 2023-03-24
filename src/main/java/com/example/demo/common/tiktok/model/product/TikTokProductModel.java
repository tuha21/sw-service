package com.example.demo.common.tiktok.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TikTokProductModel {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("status")
    private int status;

    @JsonProperty("skus")
    private List<TiktokProductSkuModel> skus;

    @JsonProperty("sale_regions")
    private List<String> saleRegions;

    @JsonProperty("create_time")
    private long createTime;

    @JsonProperty("update_time")
    private long updateTime;

}
