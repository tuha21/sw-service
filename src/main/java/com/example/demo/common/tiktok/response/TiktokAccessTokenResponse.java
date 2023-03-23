package com.example.demo.common.tiktok.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokAccessTokenResponse extends TiktokBaseResponse {

    private static final long serialVersionUID = 1L;

    @JsonProperty("data")
    private AccessTokenResponse data;

    @Data
    public static class AccessTokenResponse {

        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("access_token_expire_in")
        private int accessTokenExpireIn;

        @JsonProperty("refresh_token")
        private String refreshToken;

        @JsonProperty("refresh_token_expire_in")
        private int refreshTokenExpireIn;

        @JsonProperty("open_id")
        private String openId;

        @JsonProperty("seller_name")
        private String sellerName;

    }


}
