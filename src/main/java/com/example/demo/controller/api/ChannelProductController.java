package com.example.demo.controller.api;

import com.example.demo.controller.response.BaseResponse;
import com.example.demo.service.tiktokproduct.TikTokProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/channel-product")
public class ChannelProductController {

    private final TikTokProductService tikTokProductService;

    @GetMapping("/crawl")
    public ResponseEntity<BaseResponse> crawlProduct (
        @RequestParam("connectionId") int connectionId
    ) {
        return new ResponseEntity<>(tikTokProductService.crawlProduct(connectionId), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<BaseResponse> filterProduct (
        @RequestParam("connectionIds") List<Integer> connectionIds
    ) {
        return new ResponseEntity<>(tikTokProductService.filter(connectionIds), HttpStatus.OK);
    }

}
