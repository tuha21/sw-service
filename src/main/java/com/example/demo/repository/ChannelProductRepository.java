package com.example.demo.repository;

import com.example.demo.domain.ChannelProduct;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelProductRepository extends JpaRepository<ChannelProduct, Integer> {

    ChannelProduct findByItemId(String itemId);

    List<ChannelProduct> findAllByConnectionIdInAndNameContains(List<Integer> connectionIds, String name, Pageable pageable);

    List<ChannelProduct> findAllByConnectionIdInAndNameContainsAndMappingStatus(List<Integer> connectionIds, String query, boolean mappingStatus, Pageable pageable);

    int countAllByConnectionIdInAndNameContains(List<Integer> connectionIds, String name);

    int countAllByConnectionIdInAndNameContainsAndMappingStatus(List<Integer> connectionIds, String query, boolean mappingStatus);
}
