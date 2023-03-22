package dev.app.service.connect;

import dev.app.common.consts.TikTokAppConst;
import dev.app.common.tiktok.request.TiktokAccessTokenRequest;
import dev.app.common.tiktok.response.TiktokAccessTokenResponse;
import dev.app.domain.Connection;
import dev.app.repository.ConnectionRepository;
import dev.app.repository.TenantRepository;
import dev.app.service.tiktok.TikTokApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
                            if (isNew) {
                                response.sendRedirect("http://localhost:3000/home/setting/connected?shop=" + vnShop.getShopId());
                            } else {
                                response.sendRedirect("http://localhost:3000/home/setting/reconnected?shop=" + vnShop.getShopId());
                            }
                        }
                    }
                }
            }
        }
    }

}
