package com.example.demo.common.tiktok.model.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokOrderItemModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("seller_sku")
    private String sellerSku;

    @JsonProperty("sku_cancel_reason")
    private String skuCancelReason;

    @JsonProperty("sku_cancel_user")
    private String skuCancelUser;

    @JsonProperty("sku_display_status")
    private int skuDisplayStatus;

    @JsonProperty("sku_ext_status")
    private int skuExtStatus;

    @JsonProperty("sku_id")
    private String skuId;

    @JsonProperty("sku_image")
    private String skuImage;

    @JsonProperty("sku_name")
    private String skuName;

    @JsonProperty("sku_original_price")
    private BigDecimal skuOriginalPrice;

    @JsonProperty("sku_platform_discount")
    private BigDecimal skuPlatformDiscount;

    @JsonProperty("sku_rts_time")
    private long skuRtsTime;

    @JsonProperty("sku_sale_price")
    private BigDecimal skuSalePrice;

    @JsonProperty("sku_seller_discount")
    private BigDecimal skuSellerDiscount;

    @JsonProperty("sku_type")
    private int skuType;
}
