package com.example.demo.common.tiktok.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokProductSkuDetailModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String sellerSku;

    private TiktokProductSkuPriceModel price;

    private List<TiktokProductSkuStockInfoModel> stockInfos;

    private List<TiktokSalesAttributes> salesAttributes;

}
