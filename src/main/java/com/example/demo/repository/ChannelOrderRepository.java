package com.example.demo.repository;

import com.example.demo.domain.ChannelOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelOrderRepository extends JpaRepository<ChannelOrder, Integer> {

    ChannelOrder findByConnectionIdAndOrderNumber(int connectionId, String orderNumber);

    List<ChannelOrder> findAllByConnectionIdInAndOrderNumberContains (List<Integer> connectionIds, String orderNumber, Pageable pageable);

    List<ChannelOrder> findAllByConnectionIdInAndOrderNumberContainsAndOrderStatusLike (List<Integer> connectionIds, String orderNumber, int orderStatus, Pageable pageable);


    Integer countAllByConnectionIdInAndOrderNumberContains (List<Integer> connectionIds, String orderNumber);

    Integer countAllByConnectionIdInAndOrderNumberContainsAndOrderStatusLike (List<Integer> connectionIds, String orderNumber, int orderStatus);

}
