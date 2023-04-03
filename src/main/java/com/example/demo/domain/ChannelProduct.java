package com.example.demo.domain;

import com.example.demo.domain.base.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "channel_products")
public class ChannelProduct extends BaseEntity {

    private int tenantId;
    private int connectionId;
    private String itemId;
    private String name;
    private String image;
    private Boolean mappingStatus;

}
