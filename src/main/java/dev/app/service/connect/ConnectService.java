package dev.app.service.connect;

import dev.app.common.consts.TikTokAppConst;
import dev.app.controller.response.BaseResponse;
import dev.app.controller.response.connect.ConnectData;
import dev.app.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@CrossOrigin(origins = "*")
public class ConnectService {

    private final TenantRepository tenantRepository;

    public BaseResponse getConnectLink(int tenantId) {
        var response = new BaseResponse();
        var connectData = new ConnectData();
        var tenant = tenantRepository.findById(tenantId).orElse(null);
        if (tenant != null) {
            var uuid = UUID.randomUUID().toString();
            tenant.setUuid(uuid);
            tenantRepository.save(tenant);
            connectData.setUrl(String.format("https://auth.tiktok-shops.com/oauth/authorize?app_key=%s&state=%s",
                    TikTokAppConst.APP_KEY, uuid));
        }
        response.setData(connectData);
        return response;
    }

}
