package com.example.demo.controller.api;

import com.example.demo.controller.request.TikTokFilterOrderRequest;
import com.example.demo.controller.response.BaseResponse;
import com.example.demo.service.TiktokOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tiktok-orders")
public class ChannelOrderController {

    private final TiktokOrderService tiktokOrderService;

    @GetMapping("/crawl")
    public ResponseEntity<BaseResponse> crawlOrders (
            @RequestParam("connectionIds") List<Integer> connectionIds,
            @RequestParam("fromDate") Integer fromDate,
            @RequestParam("toDate") Integer toDate
    ) {
        return new ResponseEntity<>(tiktokOrderService.getTikTokOrders(connectionIds, fromDate, toDate), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<BaseResponse> filterOrders (
            TikTokFilterOrderRequest request
    ) {
        return new ResponseEntity<>(tiktokOrderService.filterTikTokOrder(request), HttpStatus.OK);
    }

    @GetMapping("/print")
    public ResponseEntity<BaseResponse> printOrder (
        @RequestParam("orderId") int orderId
    ) {
        return new ResponseEntity<>(tiktokOrderService.print(orderId), HttpStatus.OK);
    }

    @GetMapping("/confirm")
    public ResponseEntity<BaseResponse> confirmOrder (
        @RequestParam("orderId") int orderId,
        @RequestParam("type") int type
    ) {
        return new ResponseEntity<>(tiktokOrderService.confirm(orderId, type), HttpStatus.OK);
    }

    @GetMapping("/histories")
    public ResponseEntity<BaseResponse> getHistories (
        @RequestParam("orderId") int orderId
    ) {
        return new ResponseEntity<>(tiktokOrderService.getOrderHistory(orderId), HttpStatus.OK);
    }

    @GetMapping("/print-report")
    public ResponseEntity<BaseResponse> printReport (
        @RequestParam("connectionIds") List<Integer> connectionIds,
        @RequestParam("from") long from,
        @RequestParam("to") long to
    ) {
        return new ResponseEntity<>(tiktokOrderService.printOrderReport(connectionIds, from, to), HttpStatus.OK);
    }

}
