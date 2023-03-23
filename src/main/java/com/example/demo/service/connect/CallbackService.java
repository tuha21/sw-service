package com.example.demo.service.connect;

import com.example.demo.common.consts.TikTokAppConst;
import com.example.demo.common.tiktok.request.TiktokAccessTokenRequest;
import com.example.demo.common.tiktok.response.TiktokAccessTokenResponse;
import com.example.demo.domain.Connection;
import com.example.demo.repository.ConnectionRepository;
import com.example.demo.repository.TenantRepository;
import com.example.demo.service.tiktok.TikTokApiService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CallbackService {

    private final TenantRepository tenantRepository;
    private final ConnectionRepository connectionRepository;

    private final TikTokApiService tikTokApiService;

    public void callbackTikTok(
            String code,
            String state,
            HttpServletResponse response,
            HttpServletRequest request
    ) throws IOException {
        var tenant = tenantRepository.getByUuid(state);
        if (tenant != null) {
            var accessTokenRequest = new TiktokAccessTokenRequest();
            accessTokenRequest.setAppKey(TikTokAppConst.APP_KEY);
            accessTokenRequest.setAppSecret(TikTokAppConst.APP_SECRET);
            accessTokenRequest.setAuthCode(code);
            accessTokenRequest.setGrantType("authorized_code");
            TiktokAccessTokenResponse tokenResponse = tikTokApiService.getAccessTokenTikTok(code);
            if (tokenResponse != null) {
                var accessTokenResponse = tokenResponse.getData();
                if (accessTokenResponse != null
                    && accessTokenResponse.getAccessToken() != null
                    && accessTokenResponse.getRefreshToken() != null
                ) {
                    var shop = tikTokApiService.getAuthorizedShopTikTok(accessTokenResponse.getAccessToken());
                    if (shop != null
                        && shop.getData() != null
                        && shop.getData().getShopList() != null
                    ) {
                        var vnShop = shop.getData().getShopList()
                                .stream()
                                .filter(s -> "VN".equalsIgnoreCase(s.getRegion()))
                                .findFirst().orElse(null);
                        if (vnShop != null) {
                            var connection = connectionRepository.findByTenantIdAndShopId(tenant.getId(), vnShop.getShopId());
                            var isNew = false;
                            if (connection == null) {
                                connection = new Connection();
                                isNew = true;
                            }
                            connection.setTenantId(tenant.getId());
                            connection.setName(vnShop.getShopName());
                            connection.setShopId(vnShop.getShopId());
                            connection.setAccessToken(accessTokenResponse.getAccessToken());
                            connection.setRefreshToken(accessTokenResponse.getRefreshToken());
                            connection.setAccessTokenExpiredAt(accessTokenResponse.getAccessTokenExpireIn());
                            connection.setRefreshTokenExpiredAt(accessTokenResponse.getRefreshTokenExpireIn());
                            connectionRepository.save(connection);
                            response.sendRedirect("https://tuha21.github.io/sw-frontend/");
                        }
                    }
                }
            }
        }
    }

}
