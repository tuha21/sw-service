package com.example.demo.common.tiktok.model.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokOrderModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("buyer_message")
    private String buyerMessage;

    @JsonProperty("buyer_uid")
    private String buyerUid;

    @JsonProperty("cancel_order_sla")
    private long cancelOrderSla;

    @JsonProperty("cancel_reason")
    private String cancelReason;

    @JsonProperty("cancel_user")
    private String cancelUser;

    @JsonProperty("create_time")
    private String createTime;

    @JsonProperty("delivery_option")
    private String deliveryOption;

    @JsonProperty("ext_status")
    private int extStatus;

    @JsonProperty("fulfillment_type")
    private int fulfillmentType;

    @JsonProperty("item_list")
    private List<TiktokOrderItemModel> itemList;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("order_line_list")
    private List<TiktokOrderLineModel> orderLineList;

    @JsonProperty("order_status")
    private int orderStatus;

    @JsonProperty("package_list")
    private List<TiktokOrderPackageModel> packageList;

    @JsonProperty("payment_info")
    private TiktokOrderPaymentModel paymentInfo;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("receiver_address_updated")
    private int receiverAddressUpdated;

    @JsonProperty("recipient_address")
    private TiktokOrderRecipientModel recipientAddress;

    @JsonProperty("rts_sla")
    private long rtsSla;

    @JsonProperty("rts_time")
    private long rtsTime;

    @JsonProperty("shipping_provider")
    private String shippingProvider;

    @JsonProperty("shipping_provider_id")
    private String shippingProviderId;

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("tts_sla")
    private long ttsSla;

    @JsonProperty("update_time")
    private long updateTime;
}
