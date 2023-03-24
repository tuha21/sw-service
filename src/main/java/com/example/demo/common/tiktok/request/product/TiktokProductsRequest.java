package com.example.demo.common.tiktok.request.product;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokProductsRequest implements Serializable{
    private static final long serialVersionUID = 1L;

    @JsonProperty("searchStatus")
    private Integer searchStatus;

    @JsonProperty("seller_sku_list")
    private List<String> sellerSkuList;

    @JsonProperty("create_time_from")
    private Integer createTimeFrom;

    @JsonProperty("create_time_to")
    private Integer createTimeTo;

    @JsonProperty("update_time_from")
    private Integer updateTimeFrom;

    @JsonProperty("update_time_to")
    private Integer updateTimeTo;

    @JsonProperty("page_number")
    private Integer pageNumber;

    @JsonProperty("page_size")
    private Integer pageSize;

}
