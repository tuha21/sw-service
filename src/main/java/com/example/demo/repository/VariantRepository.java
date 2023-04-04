package com.example.demo.repository;

import com.example.demo.domain.Variant;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantRepository extends JpaRepository<Variant, Integer> {

    Variant findBySku(String sku);

    List<Variant> findAllBySkuIn(List<String> sku);

    List<Variant> findAllByProductId(int productId);

}
