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
public class UpdateProductQuantityRequest {
    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("skus")
    private List<UpdateProductQuantitySkusRequest> skus;
}
