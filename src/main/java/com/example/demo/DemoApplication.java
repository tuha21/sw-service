package com.example.demo;

import com.example.demo.domain.Tenant;
import com.example.demo.repository.TenantRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class DemoApplication implements CommandLineRunner {

    @Autowired
    TenantRepository tenantRepository;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("WOW! My Fist Service is running");
        List<Tenant> tenants = tenantRepository.findAll();
        log.info("Total Merchant: {}", tenants.size());
    }
}
