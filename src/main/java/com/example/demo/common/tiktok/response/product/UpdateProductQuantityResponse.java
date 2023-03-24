package com.example.demo.common.tiktok.response.product;

import com.example.demo.common.tiktok.response.TiktokBaseResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateProductQuantityResponse extends TiktokBaseResponse {
    @JsonProperty("data")
    private UpdateProductQuantityDataResponse data;
}
