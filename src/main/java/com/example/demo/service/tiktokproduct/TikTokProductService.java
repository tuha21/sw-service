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
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TikTokProductService {

    private final TikTokApiService tikTokApiService;
    private final ConnectionRepository connectionRepository;
    private final ChannelProductRepository channelProductRepository;
    private final ChannelVariantRepository channelVariantRepository;

    public BaseResponse crawlProduct (int connectionId) {
        BaseResponse baseResponse = new BaseResponse();
        String error = null;
        try {
            var connection = connectionRepository.findById(connectionId);
            if (connection.isPresent()) {
                CompletableFuture.runAsync(() -> crawlProductFuture(connection.get()));
            } else {
                error = "Không tìm thấy thông tin gian hàng";
            }
        } catch (Exception e) {
            error = e.getMessage();
            log.error("[Tiktok Product] [crawlProduct] Error crawl product: {}", e.getMessage());
        }
        baseResponse.setError(error);
        return baseResponse;
    }

    public void crawlProductFuture (Connection connection) {
        try {
            TiktokProductsResponse response;
            int page = 1;
            int size = 20;
            long fromDate = 0;
            long toDate = Utils.getUTCTimestamp();
            do {
                response = tikTokApiService.getProductsTikTok(
                    connection.getAccessToken(),
                    connection.getShopId(),
                    Integer.parseInt(String.valueOf(fromDate)),
                    Integer.parseInt(String.valueOf(toDate)),
                    page, size);
                page++;
                if (response != null
                    && response.getData() != null
                    && !response.getData().getProducts().isEmpty()) {
                    List<TikTokProductModel> tiktokProductBaseInfos = response.getData().getProducts();
                    tiktokProductBaseInfos.forEach(item -> {
                        CompletableFuture.runAsync(() -> crawlProductDetail(connection, item.getId()));
                    });
                }
            } while (response != null
                && response.getData() != null
                && !response.getData().getProducts().isEmpty()
            );
        } catch (Exception e) {
            log.error("[Tiktok Product] [crawlProductFuture] Error crawl product: {}", e.getMessage());
        }
    }

    public void crawlProductDetail (Connection connection, String itemId) {
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
            }
            channelProduct.setConnectionId(connection.getId());
            channelProduct.setTenantId(connection.getTenantId());
            channelProduct.setItemId(itemId);
            channelProduct.setName(product.getProductName());
            try {
                channelProduct.setImage(product.getImages().get(0).getUrlList().get(0));
            } catch (Exception e) {
                log.error("crawlProductDetail | {}", e.toString());
            }
            if (!product.getSkus().isEmpty()) {
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
                    if (!sku.getSalesAttributes().isEmpty()) {
                        List<String> images = new ArrayList<>();
                        StringBuilder productName = new StringBuilder(product.getProductName());
                        for (TiktokSalesAttributes tiktokSalesAttributes : sku.getSalesAttributes()) {
                            if (tiktokSalesAttributes.getSkuImg() != null && !tiktokSalesAttributes.getSkuImg().getUrlList().isEmpty()) {
                                images.add(tiktokSalesAttributes.getSkuImg().getUrlList().get(0));
                            }
                            productName.append(" - ").append(tiktokSalesAttributes.getValueName());
                        }
                        if (!images.isEmpty()) {
                            variant.setImage(images.get(0));
                        }
                        variant.setName(productName.toString());
                    }
                    channelVariantRepository.save(variant);
                });
            }
            channelProductRepository.save(channelProduct);
        }
    }

    public BaseResponse filter (List<Integer> connectionIds) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            List<ChannelProductResponse> channelProductResponses = new ArrayList<>();
            var products = channelProductRepository.findAllByConnectionIdIn(connectionIds);
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
            log.error("filter | {}", e.toString());
            e.printStackTrace();
            baseResponse.setError(e.toString());
        }
        return baseResponse;
    }

    public void getVariant (ChannelProduct channelProduct, List<ChannelProductResponse> channelProductResponses) {
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
