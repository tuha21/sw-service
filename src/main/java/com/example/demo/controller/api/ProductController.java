package com.example.demo.controller.api;

import com.example.demo.controller.response.BaseResponse;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/filter")
    public ResponseEntity<BaseResponse> filterProduct (
            @RequestParam("tenantId") int tenantId,
            @RequestParam("query") String query,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return new ResponseEntity<>(productService.filterProduct(tenantId, query, page, limit), HttpStatus.OK);
    }

}
