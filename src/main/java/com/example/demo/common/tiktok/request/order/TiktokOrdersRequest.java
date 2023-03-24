package com.example.demo.common.tiktok.request.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokOrdersRequest {

    @JsonProperty("create_time_from")
    private int createTimeFrom;

    @JsonProperty("create_time_to")
    private int createTimeTo;

    @JsonProperty("cursor")
    private String cursor;

    @JsonProperty("end_time")
    private long endTime;

    @JsonProperty("order_status")
    private int orderStatus;

    @JsonProperty("page_size")
    private int pageSize;

    @JsonProperty("sort_by")
    private String sortBy;

    @JsonProperty("sort_type")
    private String sortType;

    @JsonProperty("start_time")
    private int startTime;

    @JsonProperty("update_time_from")
    private int updateTimeFrom;

    @JsonProperty("update_time_to")
    private int updateTimeTo;

}
