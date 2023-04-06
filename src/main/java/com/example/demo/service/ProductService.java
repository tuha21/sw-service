package com.example.demo.service;

import com.example.demo.controller.response.BaseResponse;
import com.example.demo.controller.response.ProductsResponse;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.VariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final VariantRepository variantRepository;

    public BaseResponse filterProduct (int tenantId, String query, int page, int limit) {
        BaseResponse baseResponse = new BaseResponse();
        Pageable pageable = PageRequest.of(page, limit);
        var products = productRepository.findAllByTenantIdAndNameContains(tenantId, query, pageable);
        var total = productRepository.countAllByTenantIdAndNameContains(tenantId, query);
        if (products != null) {
            products.forEach(product -> {
                var variants = variantRepository.findAllByProductId(product.getId());
                product.setVariants(variants);
            });
        }
        ProductsResponse productsResponse = new ProductsResponse();
        productsResponse.setProducts(products);
        productsResponse.setTotal(total);
        baseResponse.setData(productsResponse);
        return baseResponse;
    }

}
