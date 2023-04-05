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
public class TiktokPickUp implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("pick_up_end_time")
    private int pickUpEndTime;

    @JsonProperty("pick_up_start_time")
    private int pickUpStartTime;
}
