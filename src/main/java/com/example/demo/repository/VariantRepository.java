package com.example.demo.repository;

import com.example.demo.domain.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantRepository extends JpaRepository<Variant, Integer> {

    Variant findBySku(String sku);

}
