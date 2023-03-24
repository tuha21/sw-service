package com.example.demo.common.tiktok.response.order;

import com.example.demo.common.tiktok.response.TiktokBaseResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokOrderListResponse extends TiktokBaseResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("data")
    private TikTokOrderList data;

    @Data
    public static class TikTokOrderList {

        @JsonProperty("more")
        private boolean more;

        @JsonProperty("next_cursor")
        private String nextCursor;

        @JsonProperty("order_list")
        private List<TiktokOrderResponse> orderList;

        @JsonProperty("total")
        private int total;

        @Data
        public static class TiktokOrderResponse {

            @JsonProperty("order_id")
            private String orderId;

            @JsonProperty("order_status")
            private int orderStatus;

            @JsonProperty("update_time")
            private long updateTime;
        }
    }

}
