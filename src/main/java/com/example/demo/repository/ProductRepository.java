package com.example.demo.repository;

import com.example.demo.domain.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByTenantIdAndNameContains(int tenantId, String name, Pageable pageable);

    int countAllByTenantIdAndNameContains(int tenantId, String name);

}
