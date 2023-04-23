package com.example.demo.service.tiktokproduct;

import com.example.demo.common.tiktok.model.product.TikTokProductModel;
import com.example.demo.common.tiktok.model.product.TiktokSalesAttributes;
import com.example.demo.common.tiktok.request.product.*;
import com.example.demo.common.tiktok.response.product.TiktokProductsResponse;
import com.example.demo.controller.response.BaseResponse;
import com.example.demo.controller.response.ChannelProductResponse;
import com.example.demo.controller.response.ChannelProductsResponse;
import com.example.demo.domain.ChannelProduct;
import com.example.demo.domain.Connection;
import com.example.demo.domain.Product;
import com.example.demo.domain.Variant;
import com.example.demo.domain.base.ChannelVariant;
import com.example.demo.repository.*;
import com.example.demo.service.tiktok.TikTokApiService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TikTokProductService {

    private final TikTokApiService tikTokApiService;
    private final ConnectionRepository connectionRepository;
    private final ChannelProductRepository channelProductRepository;
    private final ChannelVariantRepository channelVariantRepository;
    private final VariantRepository variantRepository;
    private final ProductRepository productRepository;

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

    public BaseResponse getMappingInfo (int id) {
        var baseResponse = new BaseResponse();
        var channelVariantOpt = channelVariantRepository.findById(id);
        if (channelVariantOpt.isPresent()) {
            var channelVariant = channelVariantOpt.get();
            var connectionOpt = connectionRepository.findById(channelVariant.getConnectionId());
            if (connectionOpt.isPresent()) {
                var connection = connectionOpt.get();
                var productDetail = tikTokApiService.getProductDetailTikTok(
                        connection.getAccessToken(),
                        connection.getShopId(),
                        channelVariant.getItemId()
                );
                System.out.println(productDetail);
                if (productDetail != null && productDetail.getData() != null) {
                    var tiktokVariant = productDetail.getData().getSkus().stream()
                            .filter(item -> item.getId().equalsIgnoreCase(channelVariant.getVariantId()))
                            .findFirst()
                            .orElse(null);
                    assert tiktokVariant != null;
                    System.out.println("set mapping info");
                    channelVariant.setQuantity(tiktokVariant.getStockInfos().get(0).getAvailableStock());
                    channelVariant.setPrice(new BigDecimal(tiktokVariant.getPrice().getOriginalPrice()));
                }
            }
            var coreVar = variantRepository.findById(channelVariant.getMappingId());
            coreVar.ifPresent(channelVariant::setVariant);
            baseResponse.setData(channelVariant);
        }
        return baseResponse;
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
            try {
                channelProduct.setImage(product.getImages().get(0).getUrlList().get(0));
            } catch (Exception e) {
                log.error("crawlProductDetail | {}", e.toString());
            }
            channelProductRepository.save(channelProduct);
            if (!product.getSkus().isEmpty()) {
                ChannelProduct finalChannelProduct = channelProduct;
                ChannelProduct finalChannelProduct1 = channelProduct;
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
                    variant.setQuantity(sku.getStockInfos().get(0).getAvailableStock());
                    variant.setPrice(new BigDecimal(sku.getPrice().getOriginalPrice()));
                    variant.setChannelProductId(finalChannelProduct1.getId());
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
            Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
            int total = 0;
            if (mappingStatus == 1) {
                products = channelProductRepository.findAllByConnectionIdInAndNameContains(connectionIds, query, pageable);
                total = channelProductRepository.countAllByConnectionIdInAndNameContains(connectionIds, query);
            } else if(mappingStatus == 2) {
                products = channelProductRepository.findAllByConnectionIdInAndNameContainsAndMappingStatus(connectionIds, query, true, pageable);
                total = channelProductRepository.countAllByConnectionIdInAndNameContainsAndMappingStatus(connectionIds, query, true);
            } else {
                products = channelProductRepository.findAllByConnectionIdInAndNameContainsAndMappingStatus(connectionIds, query, false, pageable);
                total = channelProductRepository.countAllByConnectionIdInAndNameContainsAndMappingStatus(connectionIds, query, false);
            }
            if (products != null) {
                CompletableFuture.allOf(
                        products
                                .stream()
                                .map(product -> CompletableFuture.runAsync(() -> getVariant(product, channelProductResponses)))
                                .toArray(CompletableFuture[]::new)
                ).get();
            }
            var channelProductsResponse = new ChannelProductsResponse();
            channelProductsResponse.setTotal(total);
            channelProductsResponse.setProducts(channelProductResponses);
            baseResponse.setData(channelProductsResponse);
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
        variants.forEach(variant -> {
            if (variant.getMappingId() != 0) {
                var coreVar = variantRepository.findById(variant.getMappingId());
                coreVar.ifPresent(variant::setVariant);
            }
        });
        channelProductResponse.setVariants(variants);
        var mappingList = variants.stream().filter(variant -> variant.getMappingId() != 0).collect(
                Collectors.toList());
        channelProductResponse.setTotalMapping(mappingList.size());
        channelProductResponses.add(channelProductResponse);
    }

    public BaseResponse multiMap (List<Integer> connectionIds) {
        var response = new BaseResponse();
        var connections = connectionRepository.findAllByIdIn(connectionIds);
        try {
            if (connections != null) {
                CompletableFuture.allOf(
                        connections.stream()
                        .map(this::multiMapByConnectionFuture)
                                .toArray(CompletableFuture[]::new)
                ).get();
            }
        } catch (Exception e) {
            response.setError(e.toString());
            log.error(e.toString());
        }
        return response;
    }

    private CompletableFuture<Void> multiMapByConnectionFuture (Connection connection) {
        return CompletableFuture.runAsync(() -> multiMapByConnection(connection));
    }

    private void multiMapByConnection (Connection connection) {
           try {
               var channelVariants = channelVariantRepository.findAllByConnectionId(connection.getId());
               if (channelVariants != null) {
                   CompletableFuture.allOf(
                           channelVariants
                                   .stream()
                                   .map(this::multiMapByChannelVariantFuture)
                                   .toArray(CompletableFuture[]::new)
                   ).get();
               }
           } catch (Exception e) {
               log.error("multiMapByConnection: {}", e.toString());
           }
    }

    private CompletableFuture<Void> multiMapByChannelVariantFuture (ChannelVariant channelVariant) {
        return CompletableFuture.runAsync(() -> multiMapByChannelVariant(channelVariant));
    }

    private void multiMapByChannelVariant (ChannelVariant channelVariant) {
        try {
            var variant = variantRepository.findAllBySku(channelVariant.getSku());
            if (variant != null && variant.size() > 0) {
                channelVariant.setMappingId(variant.get(0).getId());
                channelVariantRepository.save(channelVariant);
                processProductMapping(channelVariant.getItemId());
            }
        } catch (Exception e) {
            log.error("multiMapByChannelVariant: {}", e.toString());
        }
    }

    public BaseResponse quickMapProduct (int tiktokVariantId) {
        var response = new BaseResponse();
        var tiktokVariantOptional = channelVariantRepository.findById(tiktokVariantId);
        if (tiktokVariantOptional.isPresent()) {
            if (tiktokVariantOptional.get().getSku() == null) {
                response.setError("Sản phẩm phải có sku mới có thể liên kết nhanh");
            } else {
                var variant = variantRepository.findAllBySku(tiktokVariantOptional.get().getSku());
                if (variant != null && variant.size() > 0) {
                    ChannelVariant tiktokVariant = tiktokVariantOptional.get();
                    tiktokVariant.setMappingId(variant.get(0).getId());
                    channelVariantRepository.save(tiktokVariant);
                    processProductMapping(tiktokVariant.getItemId());
                } else {
                    response.setError("Không tìm thấy sản phẩm có SKU tương ứng");
                }
            }
        }
        return response;
    }

    public BaseResponse manualMapProduct (int tiktokVariantId, int variantId) {
        var response = new BaseResponse();
        var tiktokVariantOptional = channelVariantRepository.findById(tiktokVariantId);
        if (tiktokVariantOptional.isPresent()) {
            var variantOptional = variantRepository.findById(variantId);
            if (variantOptional.isPresent()) {
                ChannelVariant tiktokVariant = tiktokVariantOptional.get();
                tiktokVariant.setMappingId(variantOptional.get().getId());
                channelVariantRepository.save(tiktokVariant);
                processProductMapping(tiktokVariant.getItemId());
            } else {
                response.setError("Không tìm thấy sản phẩm");
            }
        }
        return response;
    }

    public BaseResponse unMapProduct (int tiktokVariantId) {
        var response = new BaseResponse();
        var tiktokVariantOptional = channelVariantRepository.findById(tiktokVariantId);
        if (tiktokVariantOptional.isPresent()) {
            ChannelVariant tiktokVariant = tiktokVariantOptional.get();
            tiktokVariant.setMappingId(0);
            channelVariantRepository.save(tiktokVariant);
            processProductMapping(tiktokVariant.getItemId());
        }
        return response;
    }

    @Async
    public void processProductMapping (String itemId) {
        try {
            var channelProduct = channelProductRepository.findByItemId(itemId);
            if (channelProduct != null) {
                var channelVariants = channelVariantRepository.findAllByItemId(channelProduct.getItemId());
                if (channelVariants != null) {
                    var isMapping = channelVariants.stream().allMatch(item -> item.getMappingId() != 0);
                    channelProduct.setMappingStatus(isMapping);
                    channelProductRepository.save(channelProduct);
                }
            }
        } catch (Exception e) {
            log.error("processProductMapping | {}", e.toString());
        }
    }

    public BaseResponse create (int id) {
        var response = new BaseResponse();
        try {
            var channelVariantToCreate = channelVariantRepository.findById(id);
            if (channelVariantToCreate.isPresent()) {
                var variantOld = variantRepository.findAllBySku(channelVariantToCreate.get().getSku());
                if (variantOld != null) {
                    response.setError("SKU đã tồn tại");
                }
                var channelProductOptional = channelProductRepository.findById(channelVariantToCreate.get().getChannelProductId());
                if (channelProductOptional.isPresent()) {
                    var channelProduct = channelProductOptional.get();
                    var channelVariants = channelVariantRepository.findAllByItemId(channelProduct.getItemId());
                    if (channelVariants != null && channelVariants.size() > 0) {
                        Product product;
                        List<String> skus = channelVariants.stream().map(ChannelVariant::getSku).collect(Collectors.toList());
                        var variants = variantRepository.findAllBySkuIn(skus);
                        if (variants != null && variants.size() > 0) {
                            var productOptional = productRepository.findById(variants.get(0).getProductId());
                            product = productOptional.orElseGet(Product::new);
                        } else {
                            product = new Product();
                        }
                        product.setTenantId(channelProduct.getTenantId());
                        product.setImage(channelProduct.getImage());
                        product.setName(channelProduct.getName());
                        productRepository.save(product);
                        Product finalProduct = product;
                        if (variants != null && variants.size() > 0) {
                            channelVariants.forEach(channelVariant -> {
                                var variant = variants.stream().filter(item -> item.getSku().equalsIgnoreCase(channelVariant.getSku())).findFirst().orElse(null);
                                if (variant == null) {
                                    variant = new Variant();
                                }
                                variant.setProductId(finalProduct.getId());
                                variant.setSku(channelVariant.getSku());
                                variant.setName(channelVariant.getName());
                                variant.setOnHand(channelVariant.getQuantity());
                                variant.setAvailable(channelVariant.getQuantity());
                                variant.setCommited(channelVariant.getQuantity());
                                variant.setCostPrice(channelVariant.getPrice());
                                variant.setImportPrice(channelVariant.getPrice());
                                variant.setRetailPrice(channelVariant.getPrice());
                                variant.setWholePrice(channelVariant.getPrice());
                                variant.setImage(channelVariant.getImage());
                                variantRepository.save(variant);
                            });
                        } else {
                            List<Variant> newVariants = channelVariants.stream().map(channelVariant -> {
                                Variant newVariant = new Variant();
                                newVariant.setProductId(finalProduct.getId());
                                newVariant.setSku(channelVariant.getSku());
                                newVariant.setName(channelVariant.getName());
                                newVariant.setOnHand(channelVariant.getQuantity());
                                newVariant.setAvailable(channelVariant.getQuantity());
                                newVariant.setCommited(channelVariant.getQuantity());
                                newVariant.setCostPrice(channelVariant.getPrice());
                                newVariant.setImportPrice(channelVariant.getPrice());
                                newVariant.setRetailPrice(channelVariant.getPrice());
                                newVariant.setWholePrice(channelVariant.getPrice());
                                newVariant.setImage(channelVariant.getImage());
                                return newVariant;
                            }).collect(Collectors.toList());
                            variantRepository.saveAll(newVariants);
                        }
                    }
                }
            }
        } catch (Exception e) {
            response.setError(e.toString());
            e.printStackTrace();
        }
        return response;
    }

    public BaseResponse multiSync (List<Integer> connectionIds) {
        var connections = connectionRepository.findAllByIdIn(connectionIds);
        if (connections != null) {
            CompletableFuture.allOf(
                    connections
                            .stream()
                            .map(this::syncByConnectionFuture)
                            .toArray(CompletableFuture[]::new)
            );
        }
        return new BaseResponse();
    }

    public CompletableFuture<Void> syncByConnectionFuture (Connection connection) {
        return CompletableFuture.runAsync(() -> syncByConnection(connection));
    }

    public void syncByConnection (Connection connection) {
        var channelProducts = channelProductRepository.findAllByConnectionId(connection.getId());
        CompletableFuture.allOf(
                channelProducts
                        .stream()
                        .map(item -> syncFuture(item.getId()))
                        .toArray(CompletableFuture[]::new)
        );
    }

    public CompletableFuture<Void> syncFuture (int id) {
        return CompletableFuture.runAsync(() -> sync(id));
    }

    public BaseResponse sync (int id) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            var channelProductOptional = channelProductRepository.findById(id);
            if (channelProductOptional.isPresent()) {
                var channelProduct = channelProductOptional.get();
                var connectionOpt = connectionRepository.findById(channelProduct.getConnectionId());
                if (connectionOpt.isPresent()) {
                    var channelVariants = channelVariantRepository.findAllByItemId(channelProduct.getItemId());
                    if (channelVariants != null) {
                        UpdateProductPriceRequest updateProductPriceRequest = new UpdateProductPriceRequest();
                        UpdateProductQuantityRequest updateProductQuantityRequest = new UpdateProductQuantityRequest();

                        List<UpdateProductPriceSkusRequest> priceRequests = new ArrayList<>();
                        List<UpdateProductQuantitySkusRequest> qtyRequests = new ArrayList<>();
                        for (ChannelVariant channelVariant : channelVariants) {
                            if (channelVariant.getMappingId() != 0) {
                                var variantOpt = variantRepository.findById(channelVariant.getMappingId());
                                if (variantOpt.isPresent()) {
                                    UpdateProductPriceSkusRequest updateProductPriceSkusRequest = new UpdateProductPriceSkusRequest();
                                    updateProductPriceSkusRequest.setId(channelVariant.getVariantId());
                                    updateProductPriceSkusRequest.setOriginalPrice(variantOpt.get().getRetailPrice().toString());
                                    priceRequests.add(updateProductPriceSkusRequest);

                                    UpdateProductQuantitySkusRequest updateProductQuantitySkusRequest = new UpdateProductQuantitySkusRequest();
                                    updateProductQuantitySkusRequest.setId(channelVariant.getVariantId());
                                    var stockRequest = new UpdateProductQuantityStockInfosRequest();
                                    stockRequest.setAvailableStock(variantOpt.get().getAvailable());
                                    stockRequest.setWareHouseId(connectionOpt.get().getWarehouseId());
                                    updateProductQuantitySkusRequest.setStockInfos(Collections.singletonList(stockRequest));
                                    qtyRequests.add(updateProductQuantitySkusRequest);
                                }
                            }
                        }
                        updateProductPriceRequest.setProductId(channelProduct.getItemId());
                        updateProductQuantityRequest.setProductId(channelProduct.getItemId());
                        updateProductPriceRequest.setSkus(priceRequests);
                        updateProductQuantityRequest.setSkus(qtyRequests);
                        var priceResponse = tikTokApiService.updatePrice(connectionOpt.get().getAccessToken(), connectionOpt.get().getShopId(), updateProductPriceRequest);
                        var qtyResponse = tikTokApiService.updateQty(connectionOpt.get().getAccessToken(), connectionOpt.get().getShopId(), updateProductQuantityRequest);
                        String error = null;
                        if (priceResponse != null) {
                            if (priceResponse.getData() != null
                                && (priceResponse.getData().getFailedSKuIds() == null || priceResponse.getData().getFailedSKuIds().isEmpty())
                            ) {
                                System.out.println("update price success");
                            } else {
                                if (priceResponse.getMessage() != null) {
                                    error = priceResponse.getMessage();
                                }
                            }
                        }
                        if (qtyResponse != null) {
                            if (qtyResponse.getData() != null
                                    && (qtyResponse.getData().getFailedSkus() == null || qtyResponse.getData().getFailedSkus().isEmpty())
                            ) {
                                System.out.println("update quantiry success");
                            } else {
                                if (qtyResponse.getMessage() != null) {
                                    if (error == null) {
                                        error = qtyResponse.getMessage();
                                    } else {
                                        error += qtyResponse.getMessage();
                                    }
                                }
                            }
                        }
                        baseResponse.setError(error);
                    }
                }
            }
        } catch (Exception e) {
            baseResponse.setError(e.toString());
        }
        return baseResponse;
    }

}
