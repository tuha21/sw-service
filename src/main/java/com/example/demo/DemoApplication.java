package com.example.demo;

import com.example.demo.domain.Tenant;
import com.example.demo.repository.TenantRepository;
import java.util.List;

import com.example.demo.service.TiktokOrderService;
import com.example.demo.service.tiktok.TikTokApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableAsync
@EnableScheduling
@Slf4j
public class DemoApplication implements CommandLineRunner {

    @Autowired
    TenantRepository tenantRepository;

    @Autowired
    TiktokOrderService tiktokOrderService;

    @Autowired
    TikTokApiService tikTokApiService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var response = tiktokOrderService.getTikTokOrders(4, 1672531200, 1680307200);
        System.out.println(response);
    }
}
