package com.example.demo.domain;

import com.example.demo.domain.base.BaseEntity;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "channel_order_items")
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

    @Transient
    private Variant variant;

}
