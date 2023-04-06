package com.example.demo.service.tiktok;

import com.example.demo.common.consts.TikTokAppConst;
import com.example.demo.common.tiktok.feign.TikTokAuthFeign;
import com.example.demo.common.tiktok.feign.TiktokFulfillmentFeign;
import com.example.demo.common.tiktok.feign.TiktokLogisticsFeign;
import com.example.demo.common.tiktok.feign.TiktokOrderFeign;
import com.example.demo.common.tiktok.feign.TiktokProductFeign;
import com.example.demo.common.tiktok.feign.TiktokShopFeign;
import com.example.demo.common.tiktok.request.TiktokAccessTokenRequest;
import com.example.demo.common.tiktok.request.TiktokBaseRequest;
import com.example.demo.common.tiktok.request.fulfillment.TiktokShipPackageRequest;
import com.example.demo.common.tiktok.request.order.TiktokFilterOrderRequest;
import com.example.demo.common.tiktok.request.order.TiktokOrderDetailsRequest;
import com.example.demo.common.tiktok.request.product.TiktokProductsRequest;
import com.example.demo.common.tiktok.response.TiktokAccessTokenResponse;
import com.example.demo.common.tiktok.response.TiktokAuthorizedShopResponse;
import com.example.demo.common.tiktok.response.fulfillment.TiktokShipPackageReponse;
import com.example.demo.common.tiktok.response.logistics.shippingdocument.TiktokShippingDocumentResponse;
import com.example.demo.common.tiktok.response.logistics.shippinginfo.TiktokShippingInfoResponse;
import com.example.demo.common.tiktok.response.order.TiktokOrderDetailsResponse;
import com.example.demo.common.tiktok.response.order.TiktokOrdersResponse;
import com.example.demo.common.tiktok.response.product.TiktokProductDetailResponse;
import com.example.demo.common.tiktok.response.product.TiktokProductsResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TikTokApiService {

    private final TikTokAuthFeign tikTokAuthFeign;
    private final TiktokShopFeign tiktokShopFeign;
    private final TiktokProductFeign tiktokProductFeign;

    private final TiktokOrderFeign tiktokOrderFeign;
    private final TiktokLogisticsFeign tiktokLogisticsFeign;
    private final TiktokFulfillmentFeign tiktokFulfillmentFeign;

    public TiktokAccessTokenResponse getAccessTokenTikTok(String code) {
        try {
            var accessTokenRequest = new TiktokAccessTokenRequest();
            accessTokenRequest.setAppKey(TikTokAppConst.APP_KEY);
            accessTokenRequest.setAppSecret(TikTokAppConst.APP_SECRET);
            accessTokenRequest.setAuthCode(code);
            accessTokenRequest.setGrantType("authorized_code");
            return tikTokAuthFeign.getAccessToken(accessTokenRequest).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TiktokAuthorizedShopResponse getAuthorizedShopTikTok (String accessToken) {
        try {
            return tiktokShopFeign.getAuthorizedShop(TikTokAppConst.APP_KEY, accessToken).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TiktokProductsResponse getProductsTikTok (String accessToken, String shopId, Integer fromDate, Integer toDate, int page, int size) {
        try {
            var requestBody = new TiktokProductsRequest();
            requestBody.setUpdateTimeFrom(fromDate);
            requestBody.setUpdateTimeTo(toDate);
            requestBody.setPageNumber(page);
            requestBody.setPageSize(size);
            return tiktokProductFeign.getProductList(
                TikTokAppConst.APP_KEY,
                accessToken,
                shopId,
                requestBody
            ).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TiktokProductDetailResponse getProductDetailTikTok (String accessToken, String shopId, String itemId) {
        try {
            var requestParams = new TiktokBaseRequest();
            requestParams.setAccessToken(accessToken);
            requestParams.setShopId(shopId);
            return tiktokProductFeign.getProductDetail(TikTokAppConst.APP_KEY, accessToken, shopId, itemId).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TiktokOrdersResponse getOrdersTiktok (String token, String shopId, TiktokFilterOrderRequest request) {
        try {
            var response = tiktokOrderFeign.filterOrder(TikTokAppConst.APP_KEY, token, shopId, request).getBody();
            if (response != null) {
                if (response.getMessage() != null) {
                    log.error(response.getMessage());
                }
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TiktokOrderDetailsResponse getOrderDetailsTiktok (String token, String shopId, TiktokOrderDetailsRequest request) {
        try {
            var response = tiktokOrderFeign.getDetails(TikTokAppConst.APP_KEY, token, shopId, request).getBody();
            if (response != null) {
                if (response.getMessage() != null) {
                    log.error(response.getMessage());
                }
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TiktokShippingDocumentResponse getShippingDocument (String token, String shopId, String orderNumber, String type) {
        try {
            var response = tiktokLogisticsFeign.getShippingDocument(TikTokAppConst.APP_KEY, token, shopId, orderNumber, type).getBody();
            if (response != null) {
                if (response.getMessage() != null) {
                    log.error(response.getMessage());
                }
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TiktokShipPackageReponse shipPackage(String token, String shopId, TiktokShipPackageRequest request) {
        try {
            var tiktokBaseRequest = new TiktokBaseRequest();
            tiktokBaseRequest.setAccessToken(token);
            tiktokBaseRequest.setShopId(shopId);

            var response = tiktokFulfillmentFeign.shipPackage(TikTokAppConst.APP_KEY, token, shopId, request).getBody();
            if (response != null) {
                if (response.getMessage() != null) {
                    log.error(response.getMessage());
                }
                return response;
            }
        } catch (FeignException e) {
            log.error("shipPackage | {}", e.contentUTF8());
        } catch (Exception ex) {
            log.error("ERROR | {}", ex.toString());
        }
        return null;
    }

    public TiktokShippingInfoResponse getShippingInfo (String token, String shopId, String orderNumber) {
        try {
            var response = tiktokLogisticsFeign.getShippingInfo(TikTokAppConst.APP_KEY, token, shopId, orderNumber).getBody();
            if (response != null) {
                if (response.getMessage() != null) {
                    log.error(response.getMessage());
                }
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
