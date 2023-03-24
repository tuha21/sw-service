package com.example.demo.common.tiktok.response.product;

import com.example.demo.common.tiktok.model.product.TiktokProductDetailModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokProductDetailResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("data")
    private TiktokProductDetailModel data;

}
