package com.example.demo.common.tiktok.feign;

import com.example.demo.common.tiktok.response.TiktokAuthorizedShopResponse;
import com.example.demo.configuration.TiktokFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(
    name = "TikTokShopFeign",
    url = "https://open-api.tiktokglobalshop.com",
    configuration = TiktokFeignConfig.class
)
public interface TiktokShopFeign {

    @GetMapping(
        value = "/api/shop/get_authorized_shop",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    ResponseEntity<TiktokAuthorizedShopResponse> getAuthorizedShop(
        @RequestParam("app_key") String partnerId,
        @RequestParam("access_token") String accessToken
    );

}
