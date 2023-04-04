package com.example.demo.domain;

import com.example.demo.domain.base.BaseEntity;
import java.math.BigDecimal;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "variants")
public class Variant extends BaseEntity {

    private int productId;

    private String sku;

    private String name;

    private String image;

    private int onHand;

    private int available;

    private int commited;

    private BigDecimal retailPrice;

    private BigDecimal wholePrice;

    private BigDecimal importPrice;

    private BigDecimal costPrice;

}
