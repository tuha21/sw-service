package com.example.demo.common.tiktok.model.fulfillment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokPickupTimeListModel {

    @JsonProperty("avaliable")
    private String available;

    @JsonProperty("end_time")
    private long endTime;

    @JsonProperty("start_time")
    private long startTime;

}
