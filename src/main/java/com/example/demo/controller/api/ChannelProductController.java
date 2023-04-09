package com.example.demo.controller.api;

import com.example.demo.controller.response.BaseResponse;
import com.example.demo.service.tiktokproduct.TikTokProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/channel-product")
public class ChannelProductController {

    private final TikTokProductService tikTokProductService;

    @GetMapping("/crawl")
    public ResponseEntity<BaseResponse> crawlProduct (
        @RequestParam("connectionIds") List<Integer> connectionIds,
        @RequestParam("fromDate") Integer fromDate,
        @RequestParam("toDate") Integer toDate
    ) {
        return new ResponseEntity<>(tikTokProductService.crawlProduct(connectionIds, fromDate, toDate), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<BaseResponse> filterProduct (
        @RequestParam("connectionIds") List<Integer> connectionIds,
        @RequestParam("page") int page,
        @RequestParam("mappingStatus") int mappingStatus,
        @RequestParam("query") String query
    ) {
        return new ResponseEntity<>(tikTokProductService.filter(connectionIds, page, mappingStatus, query), HttpStatus.OK);
    }

    @GetMapping("/quick-map")
    public ResponseEntity<BaseResponse> quickMap (
            @RequestParam("id") int id
    ) {
        return new ResponseEntity<>(tikTokProductService.quickMapProduct(id), HttpStatus.OK);
    }

    @GetMapping("/manual-map")
    public ResponseEntity<BaseResponse> manualMap (
            @RequestParam("id") int id,
            @RequestParam("variantId") int variantId
    ) {
        return new ResponseEntity<>(tikTokProductService.manualMapProduct(id, variantId), HttpStatus.OK);
    }

    @GetMapping("/un-map")
    public ResponseEntity<BaseResponse> unMap (
        @RequestParam("id") int id
    ) {
        return new ResponseEntity<>(tikTokProductService.unMapProduct(id), HttpStatus.OK);
    }

    @GetMapping("/create")
    public ResponseEntity<BaseResponse> create (
        @RequestParam("id") int id
    ) {
        return new ResponseEntity<>(tikTokProductService.create(id), HttpStatus.OK);
    }

}
