package com.example.demo.repository;

import com.example.demo.domain.Connection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Integer> {
    Connection findByTenantIdAndShopId(int tenantId, String shopId);
    List<Connection> findAllByTenantId(int tenantId);
    List<Connection> findAllByIdIn(List<Integer> ids);
}
