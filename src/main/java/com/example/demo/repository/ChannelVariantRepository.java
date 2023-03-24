package com.example.demo.repository;

import com.example.demo.domain.base.ChannelVariant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelVariantRepository extends JpaRepository<ChannelVariant, Integer> {

    ChannelVariant findByItemIdAndAndVariantId (String itemId, String variantId);

    List<ChannelVariant> findAllByItemId(String itemId);

}
