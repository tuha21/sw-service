package com.example.demo.common.tiktok.model.fulfillment;

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
public class TiktokPackagePickupConfigModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("drop_off_point_url")
    private String dropOffPointUrrl;

    @JsonProperty("is_drop_off")
    private boolean isDropOff;

    @JsonProperty("is_pick_up")
    private boolean isPickUp;

    @JsonProperty("pick_up_time_list")
    private TiktokPickupTimeListModel pickUpTimeList;

}
