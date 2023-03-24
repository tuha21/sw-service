package com.example.demo.common.tiktok.model.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokOrderLineModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("order_line_id")
    private String orderLineId;

    @JsonProperty("sku_id")
    private String skuId;
}
