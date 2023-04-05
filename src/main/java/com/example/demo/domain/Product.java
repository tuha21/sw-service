package com.example.demo.domain;

import com.example.demo.domain.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Data
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    private String name;
    private String image;
    private int tenantId;

    @Transient
    private List<Variant> variants;

}
