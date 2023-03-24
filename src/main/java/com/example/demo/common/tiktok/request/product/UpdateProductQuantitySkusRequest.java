package com.example.demo.common.tiktok.request.product;

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
public class UpdateProductQuantitySkusRequest {
    @JsonProperty("id")
    private String id;

    @JsonProperty("stock_infos")
    private List<UpdateProductQuantityStockInfosRequest> stockInfos;
}
