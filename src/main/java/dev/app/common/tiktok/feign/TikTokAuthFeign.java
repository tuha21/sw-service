package dev.app.common.tiktok.feign;

import dev.app.configuration.TiktokFeignConfig;
import dev.app.common.tiktok.request.TiktokAccessTokenRequest;
import dev.app.common.tiktok.response.TiktokAccessTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(
        name = "TikTokAuthFeign",
        url = "https://auth.tiktok-shops.com",
        configuration = TiktokFeignConfig.class
)
public interface TikTokAuthFeign {

    @PostMapping(
            value = "/api/token/getAccessToken",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    ResponseEntity<TiktokAccessTokenResponse> getAccessToken(
            @RequestBody TiktokAccessTokenRequest tikTokAccessTokenRequest
    );

}
