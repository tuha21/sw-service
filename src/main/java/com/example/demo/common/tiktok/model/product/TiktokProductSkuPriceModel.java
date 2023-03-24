package com.example.demo.common.tiktok.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokProductSkuPriceModel {

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("original_price")
    private String originalPrice;

    @JsonProperty("price_include_vat")
    private String priceIncludeVat;

}
