package com.example.demo.common.tiktok.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokProductSkuModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private String id;

    @JsonProperty("seller_sku")
    private String sellerSku;

    @JsonProperty("price")
    private TiktokProductSkuPriceModel price;

    @JsonProperty("stock_infos")
    private List<TiktokProductSkuStockInfoModel> stockInfos;

}
