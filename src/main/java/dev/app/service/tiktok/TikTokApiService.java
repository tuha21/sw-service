package dev.app.service.tiktok;

import dev.app.common.consts.TikTokAppConst;
import dev.app.common.tiktok.feign.TikTokAuthFeign;
import dev.app.common.tiktok.feign.TiktokShopFeign;
import dev.app.common.tiktok.request.TiktokAccessTokenRequest;
import dev.app.common.tiktok.response.TiktokAccessTokenResponse;
import dev.app.common.tiktok.response.TiktokAuthorizedShopResponse;
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
