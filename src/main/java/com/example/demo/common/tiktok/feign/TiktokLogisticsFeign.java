package com.example.demo.common.tiktok.feign;

import com.example.demo.common.tiktok.response.logistics.shippingdocument.TiktokShippingDocumentResponse;
import com.example.demo.common.tiktok.response.logistics.shippinginfo.TiktokShippingInfoResponse;
import com.example.demo.configuration.TiktokFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(
    name = "TikTokLogisticsFeign",
    url = "https://open-api.tiktokglobalshop.com",
    configuration = TiktokFeignConfig.class
)
public interface TiktokLogisticsFeign {

    @GetMapping(value = "/api/logistics/shipping_document",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TiktokShippingDocumentResponse> getShippingDocument(
        @RequestParam("app_key") String partnerId,
        @RequestParam("access_token") String accessToken,
        @RequestParam("shop_id") String shopId,
        @RequestParam("order_id") String orderId,
        @RequestParam("document_type") String documentType
    );

    @GetMapping(value = "/api/logistics/ship/get",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TiktokShippingInfoResponse> getShippingInfo(
        @RequestParam("app_key") String partnerId,
        @RequestParam("access_token") String accessToken,
        @RequestParam("shop_id") String shopId,
        @RequestParam("order_id") String orderId
    );

}
