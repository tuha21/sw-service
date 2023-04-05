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
public class TiktokShipPackageRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("package_id")
    private String packageId;

    @JsonProperty("pick_up")
    private TiktokPickUp pickUp;

    @JsonProperty("pick_up_type")
    private int pickUpType;

    @JsonProperty("self_shipment")
    private TiktokSelfShipment selfShipment;

}