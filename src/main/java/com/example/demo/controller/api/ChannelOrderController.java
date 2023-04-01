package com.example.demo.controller.api;

import com.example.demo.controller.response.BaseResponse;
import com.example.demo.service.TiktokOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/channel-orders")
public class ChannelOrderController {

    private final TiktokOrderService tiktokOrderService;

    @GetMapping("/crawl")
    public ResponseEntity<BaseResponse> crawlOrders (
            @RequestParam int connectionId,
            @RequestParam Integer fromDate,
            @RequestParam Integer toDate
    ) {
        return new ResponseEntity<>(tiktokOrderService.getTikTokOrders(connectionId, fromDate, toDate), HttpStatus.OK);
    }

}
