package com.example.demo.common.tiktok.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokAuthorizedShopResponse extends TiktokBaseResponse {
    private static final long serialVersionUID = 1L;

    @JsonProperty("data")
    private ShopList data;

    @Data
    public static class ShopList {

        @JsonProperty("shop_list")
        private List<Shop> shopList;

        @Data
        public static class Shop {
            @JsonProperty("shop_id")
            private String shopId;

            @JsonProperty("shop_name")
            private String shopName;

            @JsonProperty("region")
            private String region;

            @JsonProperty("type")
            private int type;
        }

    }

}
