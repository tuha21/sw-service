package com.example.demo.domain.base;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "channel_variants")
public class ChannelVariant extends BaseEntity {

    private int tenantId;
    private int connectionId;
    private String sku;
    private String image;
    private String name;
    private String itemId;
    private int mappingId;
    private String variantId;

}
