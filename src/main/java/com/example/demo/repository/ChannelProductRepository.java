package com.example.demo.repository;

import com.example.demo.domain.ChannelProduct;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelProductRepository extends JpaRepository<ChannelProduct, Integer> {

    ChannelProduct findByItemId(String itemId);

    List<ChannelProduct> findAllByConnectionIdIn(List<Integer> connectionIds);

}
