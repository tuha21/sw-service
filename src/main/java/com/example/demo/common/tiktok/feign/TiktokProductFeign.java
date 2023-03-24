package com.example.demo.common.tiktok.feign;

import com.example.demo.common.tiktok.request.product.TiktokProductsRequest;
import com.example.demo.common.tiktok.request.product.UpdateProductPriceRequest;
import com.example.demo.common.tiktok.request.product.UpdateProductQuantityRequest;
import com.example.demo.common.tiktok.response.product.TiktokProductDetailResponse;
import com.example.demo.common.tiktok.response.product.TiktokProductsResponse;
import com.example.demo.common.tiktok.response.product.UpdateProductPriceResponse;
import com.example.demo.common.tiktok.response.product.UpdateProductQuantityResponse;
import com.example.demo.configuration.TiktokFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(
        name = "TikTokProductFeign",
        url = "https://open-api.tiktokglobalshop.com",
        configuration = TiktokFeignConfig.class
)
public interface TiktokProductFeign {

    @PostMapping(value = "/api/products/search",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TiktokProductsResponse> getProductList(
        @RequestParam("app_key") String partnerId,
        @RequestParam("access_token") String accessToken,
        @RequestParam("shop_id") String shopId,
        @RequestBody TiktokProductsRequest tiktokProductsRequest
    );


    @GetMapping(value = "/api/products/details",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<TiktokProductDetailResponse> getProductDetail(
        @RequestParam("app_key") String partnerId,
        @RequestParam("access_token") String accessToken,
        @RequestParam("shop_id") String shopId,
        @RequestParam("product_id") String productId
    );

    @PutMapping(value = "",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<UpdateProductPriceResponse> updatePrice(
        @RequestParam("app_key") String partnerId,
        @RequestParam("access_token") String accessToken,
        @RequestParam("shop_id") String shopId,
        @RequestBody UpdateProductPriceRequest body
    );

    @PutMapping(value ="",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<UpdateProductQuantityResponse> updateQuantity(
        @RequestParam("app_key") String partnerId,
        @RequestParam("access_token") String accessToken,
        @RequestParam("shop_id") String shopId,
        @RequestBody UpdateProductQuantityRequest body
    );
}
