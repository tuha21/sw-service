package com.example.demo.service.connect;

import com.example.demo.common.consts.TikTokAppConst;
import com.example.demo.controller.response.BaseResponse;
import com.example.demo.controller.response.connect.ConnectData;
import com.example.demo.repository.ConnectionRepository;
import com.example.demo.repository.TenantRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Slf4j
@RequiredArgsConstructor
@Service
@CrossOrigin(origins = "*")
public class ConnectService {

    private final TenantRepository tenantRepository;
    private final ConnectionRepository connectionRepository;

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

    public BaseResponse getConnections (int tenantId) {
        var response = new BaseResponse();
        var connections = connectionRepository.findAllByTenantId(tenantId);
        response.setData(connections);
        return response;
    }

}
