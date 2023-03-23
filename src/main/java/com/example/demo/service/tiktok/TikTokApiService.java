package com.example.demo.service.tiktok;

import com.example.demo.common.consts.TikTokAppConst;
import com.example.demo.common.tiktok.feign.TikTokAuthFeign;
import com.example.demo.common.tiktok.feign.TiktokShopFeign;
import com.example.demo.common.tiktok.request.TiktokAccessTokenRequest;
import com.example.demo.common.tiktok.response.TiktokAccessTokenResponse;
import com.example.demo.common.tiktok.response.TiktokAuthorizedShopResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TikTokApiService {

    private final TikTokAuthFeign tikTokAuthFeign;
    private final TiktokShopFeign tiktokShopFeign;

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
            log.error("[TikTok] Error Get Access Token: {}", e.getMessage());
        }
        return null;
    }

    public TiktokAuthorizedShopResponse getAuthorizedShopTikTok (String accessToken) {
        try {
            return tiktokShopFeign.getAuthorizedShop(TikTokAppConst.APP_KEY, accessToken).getBody();
        } catch (Exception e) {
            log.error("[TikTok] Error Get Authorized Shop: {}", e.getMessage());
        }
        return null;
    }

}
