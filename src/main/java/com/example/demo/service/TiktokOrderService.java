package com.example.demo.service;

import com.example.demo.common.tiktok.model.order.TiktokOrderModel;
import com.example.demo.common.tiktok.request.order.TiktokFilterOrderRequest;
import com.example.demo.common.tiktok.request.order.TiktokOrderDetailsRequest;
import com.example.demo.common.tiktok.response.order.TiktokOrderDetailsResponse;
import com.example.demo.common.tiktok.response.order.TiktokOrdersResponse;
import com.example.demo.common.util.Utils;
import com.example.demo.controller.response.BaseResponse;
import com.example.demo.domain.ChannelOrder;
import com.example.demo.domain.ChannelOrderItem;
import com.example.demo.domain.Connection;
import com.example.demo.domain.base.ChannelVariant;
import com.example.demo.repository.ChannelOrderItemRepository;
import com.example.demo.repository.ChannelOrderRepository;
import com.example.demo.repository.ChannelVariantRepository;
import com.example.demo.repository.ConnectionRepository;
import com.example.demo.service.tiktok.TikTokApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TiktokOrderService {

    private final TikTokApiService tikTokApiService;
    private final ConnectionRepository connectionRepository;
    private final ChannelOrderRepository channelOrderRepository;
    private final ChannelOrderItemRepository channelOrderItemRepository;
    private final ChannelVariantRepository channelVariantRepository;
    public BaseResponse getTikTokOrders(int connectionId, Integer fromDate, Integer toDate) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            TiktokFilterOrderRequest request = new TiktokFilterOrderRequest();
//            request.setUpdateTimeFrom(fromDate.longValue());
//            request.setUpdateTimeTo(toDate.longValue());
            request.setPageSize(1);
            Connection connection = connectionRepository.findById(connectionId).get();
            TiktokOrdersResponse tiktokOrdersResponse = tikTokApiService.getOrdersTiktok(
                    connection.getAccessToken(),
                    connection.getShopId(),
                    request
            );

            List<String> orderSns = new ArrayList<>();
            tiktokOrdersResponse.getData().getOrderList()
                    .forEach(item -> orderSns.add(item.getOrderId()));
            TiktokOrderDetailsRequest tiktokOrderDetailsRequest = new TiktokOrderDetailsRequest();
            tiktokOrderDetailsRequest.setOrderIdList(orderSns);
            TiktokOrderDetailsResponse tiktokOrderDetailsResponse = tikTokApiService.getOrderDetailsTiktok(
                    connection.getAccessToken(),
                    connection.getShopId(),
                    tiktokOrderDetailsRequest
            );
            List<TiktokOrderModel> tiktokOrderModels = new ArrayList<>(tiktokOrderDetailsResponse.getData().getOrderList());
            tiktokOrderModels.forEach(item -> {
                ChannelOrder channelOrder = channelOrderRepository.findByConnectionIdAndOrderNumber(connectionId, item.getOrderId());
                if (channelOrder == null) {
                    channelOrder = new ChannelOrder();
                }
                channelOrder.setConnectionId(connectionId);
                channelOrder.setOrderNumber(item.getOrderId());
                channelOrder.setOrderStatus(item.getOrderStatus());
                channelOrder.setIssuedAt(Utils.getUTCTimestamp());
                channelOrder.setTrackingCode(item.getTrackingNumber());
                channelOrder.setTotalAmount(item.getPaymentInfo().getTotalAmount());
                channelOrder.setShippingCarrier(item.getShippingProvider());
                channelOrderRepository.save(channelOrder);
                ChannelOrder finalChannelOrder = channelOrder;
                item.getItemList().forEach(line -> {
                    ChannelOrderItem channelOrderItem = channelOrderItemRepository.findByOrderIdAndItemIdAndVariantId(
                            finalChannelOrder.getId(),
                            line.getProductId(),
                            line.getSkuId()
                    );
                    if (channelOrderItem == null) {
                        channelOrderItem = new ChannelOrderItem();
                    }
                    channelOrderItem.setOrderId(finalChannelOrder.getId());
                    channelOrderItem.setSku(line.getSellerSku());
                    channelOrderItem.setQuantity(line.getQuantity());
                    channelOrderItem.setImage(line.getSkuImage());
                    channelOrderItem.setPrice(line.getSkuOriginalPrice());
                    try {
                        ChannelVariant channelVariant = channelVariantRepository.findByItemIdAndAndVariantId(
                                channelOrderItem.getItemId(),
                                channelOrderItem.getVariantId()
                        );
                        channelOrderItem.setMappingId(channelVariant.getMappingId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    channelOrderItemRepository.save(channelOrderItem);
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
            baseResponse.setError(e.getMessage());
        }
        return baseResponse;
    }

}
