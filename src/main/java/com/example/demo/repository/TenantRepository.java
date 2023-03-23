package com.example.demo.repository;

import com.example.demo.domain.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Integer> {

    Tenant getByUuid(String uuid);

}
