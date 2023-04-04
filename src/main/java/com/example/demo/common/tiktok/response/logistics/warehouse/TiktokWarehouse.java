package com.example.demo.common.tiktok.response.logistics.warehouse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokWarehouse {

    private String warehouseId;

    private String warehouseName;

    private int warehouseEffectStatus;

    private int warehouseType;

    private int warehouseSubType;

    private TiktokWarehouseAddress warehouseAddress;

}
