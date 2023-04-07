package com.example.demo;

import com.example.demo.domain.ChannelProduct;
import com.example.demo.repository.ChannelProductRepository;
import com.example.demo.repository.ChannelVariantRepository;
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

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    ChannelProductRepository channelProductRepository;

    @Autowired
    ChannelVariantRepository channelVariantRepository;

    @Override
    public void run(String... args) throws Exception {
    }
}
