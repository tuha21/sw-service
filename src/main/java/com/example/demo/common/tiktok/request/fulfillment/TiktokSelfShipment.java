package com.example.demo.common.tiktok.request.fulfillment;

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
public class TiktokSelfShipment implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("shipping_provider_id")
    private String shippingProviderId;

    @JsonProperty("tracking_number")
    private String trackingNumber;
}
