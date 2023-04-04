package com.example.demo.common.tiktok.response.logistics.shippinginfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TiktokTrackingInfoItem {

    @JsonProperty("description")
    private String description;

    @JsonProperty("update_time")
    private long updateTime;

}
