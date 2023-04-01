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
public class ChannelOrderItem extends BaseEntity {

    private int orderId;
    private String itemId;
    private String variantId;
    private String sku;
    private int quantity;
    private BigDecimal price;
    private String image;
    private int mappingId;
    private String name;

}
