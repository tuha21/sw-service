package com.example.demo.common.tiktok.response.logistics.shippinginfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TiktokShipingInfo {

    @JsonProperty("tracking_info_list")
    public List<TiktokTrackingInfoList> trackingInfoList;

}
