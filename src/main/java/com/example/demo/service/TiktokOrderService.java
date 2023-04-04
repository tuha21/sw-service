package com.example.demo.service;

import com.example.demo.common.tiktok.model.order.TiktokOrderModel;
import com.example.demo.common.tiktok.request.TiktokBaseRequest;
import com.example.demo.common.tiktok.request.order.TiktokFilterOrderRequest;
import com.example.demo.common.tiktok.request.order.TiktokOrderDetailsRequest;
import com.example.demo.common.tiktok.response.order.TiktokOrderDetailsResponse;
import com.example.demo.common.tiktok.response.order.TiktokOrdersResponse;
import com.example.demo.common.util.Utils;
import com.example.demo.controller.request.TikTokFilterOrderRequest;
import com.example.demo.controller.response.BaseResponse;
import com.example.demo.controller.response.tiktokorder.TikTokOrderData;
import com.example.demo.domain.ChannelOrder;
import com.example.demo.domain.ChannelOrderItem;
import com.example.demo.domain.Connection;
import com.example.demo.domain.base.ChannelVariant;
import com.example.demo.repository.ChannelOrderItemRepository;
import com.example.demo.repository.ChannelOrderRepository;
import com.example.demo.repository.ChannelVariantRepository;
import com.example.demo.repository.ConnectionRepository;
import com.example.demo.repository.VariantRepository;
import com.example.demo.service.tiktok.TikTokApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TiktokOrderService {

    private final TikTokApiService tikTokApiService;
    private final ConnectionRepository connectionRepository;
    private final ChannelOrderRepository channelOrderRepository;
    private final ChannelOrderItemRepository channelOrderItemRepository;
    private final ChannelVariantRepository channelVariantRepository;
    private final VariantRepository variantRepository;

    public BaseResponse getTikTokOrders(int connectionId, Integer fromDate, Integer toDate) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            var connectionOptional = connectionRepository.findById(connectionId);
            if (connectionOptional.isPresent()) {
                Connection connection = connectionOptional.get();
                List<TiktokOrderModel> tiktokOrderModels;
                String cursor = null;
                do {
                    tiktokOrderModels = new ArrayList<>();
                    TiktokFilterOrderRequest request = new TiktokFilterOrderRequest();
                    request.setUpdateTimeFrom(fromDate.longValue());
                    request.setUpdateTimeTo(toDate.longValue());
                    request.setPageSize(20);
                    request.setCursor(cursor);

                    TiktokOrdersResponse tiktokOrdersResponse = tikTokApiService.getOrdersTiktok(
                            connection.getAccessToken(),
                            connection.getShopId(),
                            request
                    );
                    if (tiktokOrdersResponse != null
                        && tiktokOrdersResponse.getData() != null
                            && tiktokOrdersResponse.getData().getOrderList() != null
                    ) {
                        cursor = tiktokOrdersResponse.getData().getNextCursor();
                        tiktokOrderModels = tiktokOrdersResponse.getData().getOrderList();
                        List<String> ids = tiktokOrderModels.stream().map(TiktokOrderModel::getOrderId).collect(Collectors.toList());
                        crawlOrderDetail(connection, ids);
                    }
                } while (!tiktokOrderModels.isEmpty());
            }
        } catch (Exception e) {
            e.printStackTrace();
            baseResponse.setError(e.getMessage());
        }
        return baseResponse;
    }

    private void crawlOrderDetail (Connection connection, List<String> ids) {
        try {
            TiktokOrderDetailsRequest tiktokOrderDetailsRequest = new TiktokOrderDetailsRequest();
            tiktokOrderDetailsRequest.setOrderIdList(ids);
            TiktokOrderDetailsResponse response = tikTokApiService.getOrderDetailsTiktok(
                    connection.getAccessToken(),
                    connection.getShopId(),
                    tiktokOrderDetailsRequest
            );
            if (response != null
                && response.getData() != null
                    && response.getData().getOrderList() != null
            ) {
                var orderList = response.getData().getOrderList();
                orderList.forEach(orderModel -> {
                    ChannelOrder channelOrder = channelOrderRepository.findByConnectionIdAndOrderNumber(connection.getId(), orderModel.getOrderId());
                    if (channelOrder == null) {
                        channelOrder = new ChannelOrder();
                    }
                    channelOrder.setConnectionId(connection.getId());
                    channelOrder.setOrderNumber(orderModel.getOrderId());
                    channelOrder.setOrderStatus(orderModel.getOrderStatus());
                    channelOrder.setIssuedAt(Utils.getUTCTimestamp());
                    channelOrder.setTrackingCode(orderModel.getTrackingNumber());
                    channelOrder.setTotalAmount(orderModel.getPaymentInfo().getTotalAmount());
                    channelOrder.setShippingCarrier(orderModel.getShippingProvider());
                    channelOrderRepository.save(channelOrder);
                    crawlChannelOrderItem(orderModel, channelOrder.getId());
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void crawlChannelOrderItem (TiktokOrderModel tiktokOrderModel, int orderId) {
        try {
            var lineItems = tiktokOrderModel.getItemList();
            lineItems.forEach(lineItem -> {
                ChannelOrderItem channelOrderItem = channelOrderItemRepository.findByOrderIdAndItemIdAndVariantId(
                        orderId,
                        lineItem.getProductId(),
                        lineItem.getSkuId()
                );
                if (channelOrderItem == null) {
                    channelOrderItem = new ChannelOrderItem();
                }
                channelOrderItem.setOrderId(orderId);
                channelOrderItem.setSku(lineItem.getSellerSku());
                channelOrderItem.setQuantity(lineItem.getQuantity());
                channelOrderItem.setImage(lineItem.getSkuImage());
                channelOrderItem.setPrice(lineItem.getSkuOriginalPrice());
                channelOrderItem.setItemId(lineItem.getProductId());
                channelOrderItem.setVariantId(lineItem.getSkuId());
                channelOrderItem.setName(lineItem.getSkuName());
                try {
                    ChannelVariant channelVariant = channelVariantRepository.findByItemIdAndAndVariantId(
                            channelOrderItem.getItemId(),
                            channelOrderItem.getVariantId()
                    );
                    if (channelVariant != null && channelVariant.getId() != 0) {
                        channelOrderItem.setMappingId(channelVariant.getMappingId());
                        var variantOptional = variantRepository.findById(channelOrderItem.getMappingId());
                        if (variantOptional.isPresent()) {
                            var variant = variantOptional.get();
                            variant.setAvailable(variant.getAvailable() - channelOrderItem.getQuantity());
                            variantRepository.save(variant);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                channelOrderItemRepository.save(channelOrderItem);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BaseResponse filterTikTokOrder (TikTokFilterOrderRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        List<ChannelOrder> channelOrders = new ArrayList<>();
        Integer count;
        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit());
        if (request.getOrderStatus() == 1) {
            channelOrders = channelOrderRepository.findAllByConnectionIdInAndOrderNumberContains(
                request.getConnectionIds(), request.getQuery(), pageable
            );
            count = channelOrderRepository.countAllByConnectionIdInAndOrderNumberContains(
                    request.getConnectionIds(), request.getQuery()
            );
        } else {
            channelOrders = channelOrderRepository.findAllByConnectionIdInAndOrderNumberContainsAndOrderStatusLike(
                    request.getConnectionIds(), request.getQuery(), request.getOrderStatus(), pageable
            );
            count = channelOrderRepository.countAllByConnectionIdInAndOrderNumberContainsAndOrderStatusLike(
                    request.getConnectionIds(), request.getQuery(), request.getOrderStatus()
            );
        }
        List<TikTokOrderData> tikTokOrderDatas = new ArrayList<>();
        channelOrders.forEach(channelOrder -> {
            TikTokOrderData tikTokOrderData = new TikTokOrderData();
            tikTokOrderData.setTiktokOrder(channelOrder);
            var channelOrderItems = channelOrderItemRepository.findAllByOrderId(channelOrder.getId());
            channelOrderItems.forEach(channelOrderItem -> {
                if (channelOrderItem.getMappingId() != 0) {
                    var variantOptional = variantRepository.findById(channelOrder.getId());
                    variantOptional.ifPresent(channelOrderItem::setVariant);
                }
            });
            tikTokOrderData.setTiktokOrderItems(channelOrderItems);
            tikTokOrderDatas.add(tikTokOrderData);
        });
        baseResponse.setData(tikTokOrderDatas);
        baseResponse.setTotal(count);
        return baseResponse;
    }

    public BaseResponse print (int orderId) {
        BaseResponse response = new BaseResponse();
        var channelOrderOptional = channelOrderRepository.findById(orderId);
        if (channelOrderOptional.isPresent()) {
            var channelOrder = channelOrderOptional.get();
            var connectionOptional = connectionRepository.findById(channelOrder.getConnectionId());
            if (connectionOptional.isPresent()) {
                var connection = connectionOptional.get();
                var tiktokBill = tikTokApiService.getShippingDocument(connection.getAccessToken(), connection.getShopId(), channelOrder.getOrderNumber(), "SL_PL");
                if (tiktokBill != null) {
                    if (tiktokBill.getData() != null && StringUtils
                        .isNotBlank(tiktokBill.getData().getDocUrl())) {
                        response.setData(tiktokBill.getData().getDocUrl());
                        channelOrder.setHasPrint(true);
                        channelOrderRepository.save(channelOrder);
                    } else if (StringUtils.isNotBlank(tiktokBill.getMessage())) {
                        response.setError(tiktokBill.getMessage());
                    } else {
                        response.setError("Có lỗi xảy ra");
                    }
                } else {
                    response.setError("Có lỗi xảy ra");
                }
            }
        }
        return response;
    }

}
