package com.example.demo.repository;

import com.example.demo.domain.ChannelOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelOrderRepository extends JpaRepository<ChannelOrder, Integer> {

    ChannelOrder findByConnectionIdAndOrderNumber(int connectionId, String orderNumber);

}
