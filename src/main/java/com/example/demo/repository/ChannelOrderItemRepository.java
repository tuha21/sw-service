package com.example.demo.repository;

import com.example.demo.domain.ChannelOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelOrderItemRepository extends JpaRepository<ChannelOrderItem, Integer> {

    ChannelOrderItem findByOrderIdAndItemIdAndVariantId(int orderId, String itemId, String variantId);

}
