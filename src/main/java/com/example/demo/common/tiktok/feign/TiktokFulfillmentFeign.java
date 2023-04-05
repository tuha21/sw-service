package com.example.demo.common.tiktok.feign;

import com.example.demo.common.tiktok.request.fulfillment.TiktokShipPackageRequest;
import com.example.demo.common.tiktok.response.fulfillment.TiktokPackagePickupConfigResponse;
import com.example.demo.common.tiktok.response.fulfillment.TiktokShipPackageReponse;
import com.example.demo.configuration.TiktokFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(
    name = "TikTokFulfillmentFeign",
    url = "https://open-api.tiktokglobalshop.com",
    configuration = TiktokFeignConfig.class
)
public interface TiktokFulfillmentFeign {

    @GetMapping(value = "/api/fulfillment/package_pickup_config/list",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TiktokPackagePickupConfigResponse> getPackagePickupConfig(
        @RequestParam("app_key") String partnerId,
        @RequestParam("access_token") String accessToken,
        @RequestParam("shop_id") String shopId,
        @RequestParam("package_id") String packageId
    );

    @PostMapping(value = "/api/fulfillment/rts",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TiktokShipPackageReponse> shipPackage(
        @RequestParam("app_key") String partnerId,
        @RequestParam("access_token") String accessToken,
        @RequestParam("shop_id") String shopId,
        @RequestBody TiktokShipPackageRequest body
    );

}
