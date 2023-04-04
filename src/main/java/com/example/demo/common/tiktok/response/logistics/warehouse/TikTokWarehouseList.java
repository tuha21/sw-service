package com.example.demo.common.tiktok.response.logistics.warehouse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TikTokWarehouseList {

    @JsonProperty("warehouse_list")
    private List<TiktokWarehouse> warehouseList;

}
