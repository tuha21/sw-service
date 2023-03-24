package com.example.demo.common.tiktok.request.order;

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
public class TiktokFilterOrderRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("start_time")
    private Long startTime;

    @JsonProperty("end_time")
    private Long endTime;

    @JsonProperty("order_status")
    private Integer orderStatus;

    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("sort_by")
    private String sortBy;

    @JsonProperty("cursor")
    private String cursor;

    @JsonProperty("create_time_from")
    private Long createTimeFrom;

    @JsonProperty("create_time_to")
    private Long createTimeTo;

    @JsonProperty("update_time_from")
    private Long updateTimeFrom;

    @JsonProperty("update_time_to")
    private Long updateTimeTo;

    @JsonProperty("sort_type")
    private Integer sortType;
}
