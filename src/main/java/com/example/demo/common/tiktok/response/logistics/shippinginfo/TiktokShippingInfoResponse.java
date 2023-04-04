package com.example.demo.common.tiktok.response.logistics.shippinginfo;

import com.example.demo.common.tiktok.response.TiktokBaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TiktokShippingInfoResponse extends TiktokBaseResponse {

    @JsonProperty("data")
    private TiktokShipingInfo data;

}
