package com.example.demo.common.tiktok.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokProductDetailModel {

    private static final long serialVersionUID = 1L;

    private String productId;

    //1-draft、2-pending、3-failed(initial creation)、4-live、5-seller_deactivated、6-platform_deactivated、7-freeze 8-deleted
    private int productStatus;

    private String productName;

    private List<TiktokCategory> categoryList;

    private TiktokBrand brand;

    private List<TiktokImage> images;

    private TiktokVideo video;

    private String description;

    private TiktokWarrantyPeriod warrantyPeriod;

    private String warrantyPolicy;

    private int packageLength;

    private int packageHeight;

    private int packageWidth;

    private String packageWeight;

    private List<TiktokProductSkuDetailModel> skus;

    private List<TiktokProductCertification> productCertifications;

    private TiktokImage sizeChart;

    private boolean isCodOpen;

    private List<TiktokProductAttribute> productAttributes;

    private TiktokQcReason qcReason;

    private long updateTime;

    private long createTime;

    private List<TiktokDeliveryService> deliveryServices;

}
