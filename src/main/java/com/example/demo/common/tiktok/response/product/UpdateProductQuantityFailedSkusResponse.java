package com.example.demo.common.tiktok.response.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateProductQuantityFailedSkusResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("failed_warehouse_ids")
    private List<String> failedWarehouseIds;
}
