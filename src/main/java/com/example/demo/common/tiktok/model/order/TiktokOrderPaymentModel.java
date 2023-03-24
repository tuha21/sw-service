package com.example.demo.common.tiktok.model.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokOrderPaymentModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("original_shipping_fee")
    private BigDecimal originalShippingFee;

    @JsonProperty("original_total_product_price")
    private BigDecimal originalTotalProductPrice;

    @JsonProperty("platform_discount")
    private BigDecimal platformDiscount;

    @JsonProperty("seller_discount")
    private BigDecimal sellerDiscount;

    @JsonProperty("shipping_fee")
    private BigDecimal shippingFee;

    @JsonProperty("shipping_fee_platform_discount")
    private BigDecimal shippingFeePlatformDiscount;

    @JsonProperty("shipping_fee_seller_discount")
    private BigDecimal shippingFeeSellerDiscount;

    @JsonProperty("sub_total")
    private BigDecimal subTotal;

    @JsonProperty("taxes")
    private BigDecimal taxes;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;
}
