package com.example.demo.domain;

import com.example.demo.domain.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "channel_orders")
public class ChannelOrder extends BaseEntity {

    private int connectionId;
    private String orderNumber;
    private int orderStatus;
    private long issuedAt;
    private String shippingCarrier;
    private String trackingCode;
    private BigDecimal totalAmount;
    private Boolean hasPrint;
    private String paymentMethod;
    private int dateKey;

}
