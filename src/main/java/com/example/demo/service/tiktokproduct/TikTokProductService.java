package com.example.demo.service.tiktokproduct;

import com.example.demo.common.tiktok.model.product.TikTokProductModel;
import com.example.demo.common.tiktok.model.product.TiktokSalesAttributes;
import com.example.demo.common.tiktok.response.product.TiktokProductsResponse;
import com.example.demo.common.util.Utils;
import com.example.demo.controller.response.BaseResponse;
import com.example.demo.controller.response.ChannelProductResponse;
import com.example.demo.domain.ChannelProduct;
import com.example.demo.domain.Connection;
import com.example.demo.domain.base.ChannelVariant;
import com.example.demo.repository.ChannelProductRepository;
import com.example.demo.repository.ChannelVariantRepository;
import com.example.demo.repository.ConnectionRepository;
import com.example.demo.service.tiktok.TikTokApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TikTokProductService {

    private final TikTokApiService tikTokApiService;
    private final ConnectionRepository connectionRepository;
    private final ChannelProductRepository channelProductRepository;
    private final ChannelVariantRepository channelVariantRepository;

    public BaseResponse crawlProduct(List<Integer> connectionIds, Integer fromDate, Integer toDate) {
        BaseResponse baseResponse = new BaseResponse();
        String error = null;
        try {
            connectionIds.forEach(connectionId -> {
                var connection = connectionRepository.findById(connectionId);
                connection.ifPresent(value -> CompletableFuture.runAsync(() -> crawlProductFuture(value, fromDate, toDate)));
            });
        } catch (Exception e) {
            error = e.getMessage();
        };
        baseResponse.setError(error);
        return baseResponse;
    }

    public void crawlProductFuture(Connection connection, Integer fromDate, Integer toDate) {
        try {
            TiktokProductsResponse response;
            int page = 1;
            int size = 20;
            do {
                response = tikTokApiService.getProductsTikTok(
                        connection.getAccessToken(),
                        connection.getShopId(),
                        fromDate,
                        toDate,
                        page, size);
                page++;
                if (response != null
                        && response.getData() != null
                        && response.getData().getProducts() != null) {
                    List<TikTokProductModel> tiktokProductBaseInfos = response.getData().getProducts();
                    tiktokProductBaseInfos.forEach(item -> {
                        CompletableFuture.runAsync(() -> crawlProductDetail(connection, item.getId()));
                    });
                }
            } while (response != null
                    && response.getData() != null
                    && response.getData().getProducts() != null
            );
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[Tiktok Product] [crawlProductFuture] Error crawl product: {}", e.getMessage());
        }
    }

    public void crawlProductDetail(Connection connection, String itemId) {
        var productDetail = tikTokApiService.getProductDetailTikTok(
                connection.getAccessToken(),
                connection.getShopId(),
                itemId
        );
        if (productDetail != null && productDetail.getData() != null) {
            var product = productDetail.getData();
            var channelProduct = channelProductRepository.findByItemId(itemId);
            if (channelProduct == null) {
                channelProduct = new ChannelProduct();
                channelProduct.setMappingStatus(false);
            }
            channelProduct.setConnectionId(connection.getId());
            channelProduct.setTenantId(connection.getTenantId());
            channelProduct.setItemId(itemId);
            channelProduct.setName(product.getProductName());
            channelProductRepository.save(channelProduct);
            try {
                channelProduct.setImage(product.getImages().get(0).getUrlList().get(0));
            } catch (Exception e) {
                log.error("crawlProductDetail | {}", e.toString());
            }
            if (!product.getSkus().isEmpty()) {
                ChannelProduct finalChannelProduct = channelProduct;
                product.getSkus().forEach(sku -> {
                    var variant = channelVariantRepository.findByItemIdAndAndVariantId(itemId, sku.getId());
                    if (variant == null) {
                        variant = new ChannelVariant();
                    }
                    variant.setTenantId(connection.getTenantId());
                    variant.setConnectionId(connection.getId());
                    variant.setItemId(itemId);
                    variant.setSku(sku.getSellerSku());
                    variant.setVariantId(sku.getId());
                    variant.setName(product.getProductName());
                    variant.setImage(finalChannelProduct.getImage());
                    if (!sku.getSalesAttributes().isEmpty()) {
                        List<String> images = new ArrayList<>();
                        StringBuilder variantName = new StringBuilder();
                        for (TiktokSalesAttributes tiktokSalesAttributes : sku.getSalesAttributes()) {
                            if (tiktokSalesAttributes.getSkuImg() != null && !tiktokSalesAttributes.getSkuImg().getUrlList().isEmpty()) {
                                images.add(tiktokSalesAttributes.getSkuImg().getUrlList().get(0));
                            }
                            if (sku.getSalesAttributes().indexOf(tiktokSalesAttributes) != 0) {
                                variantName.append(" - ");
                            }
                            variantName.append(tiktokSalesAttributes.getValueName());
                        }
                        if (!images.isEmpty()) {
                            variant.setImage(images.get(0));
                        }
                        variant.setName(variantName.toString());
                    }
                    channelVariantRepository.save(variant);
                });
            }
        }
    }

    public BaseResponse filter(List<Integer> connectionIds, int page, int mappingStatus, String query) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            List<ChannelProductResponse> channelProductResponses = new ArrayList<>();
            List<ChannelProduct> products;
            Pageable pageable = PageRequest.of(page, 10);

            if (mappingStatus == 1) {
                products = channelProductRepository.findAllByConnectionIdInAndNameContains(connectionIds, query, pageable);
            } else if(mappingStatus == 2) {
                products = channelProductRepository.findAllByConnectionIdInAndNameContainsAndMappingStatus(connectionIds, query, true, pageable);
            } else {
                products = channelProductRepository.findAllByConnectionIdInAndNameContainsAndMappingStatus(connectionIds, query, false, pageable);
            }
            if (products != null) {
                CompletableFuture.allOf(
                        products
                                .stream()
                                .map(product -> CompletableFuture.runAsync(() -> getVariant(product, channelProductResponses)))
                                .toArray(CompletableFuture[]::new)
                ).get();
            }
            baseResponse.setData(channelProductResponses);
        } catch (Exception e) {
            e.printStackTrace();
            baseResponse.setError(e.toString());
        }
        return baseResponse;
    }

    public void getVariant(ChannelProduct channelProduct, List<ChannelProductResponse> channelProductResponses) {
        ChannelProductResponse channelProductResponse = new ChannelProductResponse();
        channelProductResponse.setProduct(channelProduct);
        var variants = channelVariantRepository.findAllByItemId(channelProduct.getItemId());
        channelProductResponse.setVariants(variants);
        var mappingList = variants.stream().filter(variant -> variant.getMappingId() != 0).collect(
                Collectors.toList());
        channelProductResponse.setTotalMapping(mappingList.size());
        channelProductResponses.add(channelProductResponse);
    }

}
