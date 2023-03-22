package dev.app.repository;

import dev.app.domain.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Integer> {
    Connection findByTenantIdAndShopId (int tenantId, String shopId);
    List<Connection> findAllByTenantId (int tenantId);
}
