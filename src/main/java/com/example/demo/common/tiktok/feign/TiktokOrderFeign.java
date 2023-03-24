package com.example.demo.common.tiktok.feign;

import com.example.demo.common.tiktok.request.order.TiktokFilterOrderRequest;
import com.example.demo.common.tiktok.request.order.TiktokOrderDetailsRequest;
import com.example.demo.common.tiktok.response.order.TiktokOrderDetailsResponse;
import com.example.demo.common.tiktok.response.order.TiktokOrdersResponse;
import com.example.demo.configuration.TiktokFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(
        name = "TiktokOrderFeign",
        url = "https://open-api.tiktokglobalshop.com",
        configuration = TiktokFeignConfig.class
)
public interface TiktokOrderFeign {

    @PostMapping(value = "/api/orders/search")
    ResponseEntity<TiktokOrdersResponse> filterOrder(
        @RequestParam("app_key") String partnerId,
        @RequestParam("access_token") String accessToken,
        @RequestParam("shop_id") String shopId,
        @RequestBody TiktokFilterOrderRequest request
    );

    @PostMapping(value = "/api/orders/detail/query")
    ResponseEntity<TiktokOrderDetailsResponse> getDetails(
        @RequestParam("app_key") String partnerId,
        @RequestParam("access_token") String accessToken,
        @RequestParam("shop_id") String shopId,
        @RequestBody TiktokOrderDetailsRequest request
    );
}
