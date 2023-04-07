package com.example.demo.repository;

import com.example.demo.domain.ChannelOrder;
import com.example.demo.model.IPrintOrderReport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface ChannelOrderRepository extends JpaRepository<ChannelOrder, Integer> {

    ChannelOrder findByConnectionIdAndOrderNumber(int connectionId, String orderNumber);

    List<ChannelOrder> findAllByConnectionIdInAndOrderNumberContains (List<Integer> connectionIds, String orderNumber, Pageable pageable);

    List<ChannelOrder> findAllByConnectionIdInAndOrderNumberContainsAndOrderStatusLike (List<Integer> connectionIds, String orderNumber, int orderStatus, Pageable pageable);


    Integer countAllByConnectionIdInAndOrderNumberContains (List<Integer> connectionIds, String orderNumber);

    Integer countAllByConnectionIdInAndOrderNumberContainsAndOrderStatusLike (List<Integer> connectionIds, String orderNumber, int orderStatus);

    @Query(value = "CALL print_report_order(:connectionIds, :from, :to);", nativeQuery = true)
    List<IPrintOrderReport> printOrderReport(String connectionIds, int from, int to);

}
