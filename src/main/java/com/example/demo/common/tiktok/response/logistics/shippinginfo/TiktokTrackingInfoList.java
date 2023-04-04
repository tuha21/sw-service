package com.example.demo.common.tiktok.response.logistics.shippinginfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TiktokTrackingInfoList {

    @JsonProperty("tracking_info")
    private List<TiktokTrackingInfoItem> trackingInfoItems;

}
