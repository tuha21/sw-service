package com.example.demo.service;

import com.example.demo.common.tiktok.model.fulfillment.TiktokFailPackagesModel;
import com.example.demo.common.tiktok.model.order.TiktokOrderModel;
import com.example.demo.common.tiktok.request.TiktokBaseRequest;
import com.example.demo.common.tiktok.request.fulfillment.TiktokPickUp;
import com.example.demo.common.tiktok.request.fulfillment.TiktokSelfShipment;
import com.example.demo.common.tiktok.request.fulfillment.TiktokShipPackageRequest;
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
import com.example.demo.model.PrintReportOrderData;
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
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;

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

    public BaseResponse getTikTokOrders(List<Integer> connectionIds, Integer fromDate, Integer toDate) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            connectionIds.forEach(connectionId -> {
                crawlOrderFuture(connectionId, fromDate, toDate);
            });
        } catch (Exception e) {
            e.printStackTrace();
            baseResponse.setError(e.getMessage());
        }
        return baseResponse;
    }

    @Async
    public void crawlOrderFuture (int connectionId, Integer from, Integer to) {
        var connectionOptional = connectionRepository.findById(connectionId);
        if (connectionOptional.isPresent()) {
            Connection connection = connectionOptional.get();
            List<TiktokOrderModel> tiktokOrderModels;
            String cursor = null;
            do {
                tiktokOrderModels = new ArrayList<>();
                TiktokFilterOrderRequest request = new TiktokFilterOrderRequest();
                request.setUpdateTimeFrom(from.longValue());
                request.setUpdateTimeTo(to.longValue());
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
                    channelOrder.setIssuedAt(Long.valueOf(orderModel.getCreateTime()) / 1000);
                    channelOrder.setTrackingCode(orderModel.getTrackingNumber());
                    channelOrder.setTotalAmount(orderModel.getPaymentInfo().getTotalAmount());
                    channelOrder.setShippingCarrier(orderModel.getShippingProvider());
                    channelOrder.setPaymentMethod(orderModel.getPaymentMethod());
                    channelOrder.setDateKey(Utils.getDateKey(channelOrder.getIssuedAt()));
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
        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), Sort.by("issuedAt").descending());
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
            channelOrderItems.forEach(item -> {
                if (item.getMappingId() != 0) {
                    var variantOpt = variantRepository.findById(item.getMappingId());
                    variantOpt.ifPresent(item::setVariant);
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

    public BaseResponse confirm (int orderId, int type) {
        BaseResponse response = new BaseResponse();
        try {
            var channelOrderOptional = channelOrderRepository.findById(orderId);
            if (channelOrderOptional.isPresent()) {
                var channelOrder = channelOrderOptional.get();
                var connectionOptional = connectionRepository.findById(channelOrder.getConnectionId());
                if (connectionOptional.isPresent()) {
                    var connection = connectionOptional.get();

                    List<String> orderIdList = new ArrayList<>();
                    orderIdList.add(channelOrder.getOrderNumber());

                    var tiktokOrderDetailsRequest = new TiktokOrderDetailsRequest();
                    tiktokOrderDetailsRequest.setOrderIdList(orderIdList);
                    TiktokOrderDetailsResponse tiktokOrderDetailResponse = tikTokApiService.getOrderDetailsTiktok(connection.getAccessToken(), connection.getShopId(), tiktokOrderDetailsRequest);
                    if (tiktokOrderDetailResponse.getData() != null
                        && tiktokOrderDetailResponse.getData().getOrderList() != null
                        && tiktokOrderDetailResponse.getData().getOrderList().size() > 0
                        && tiktokOrderDetailResponse.getData().getOrderList().get(0) != null
                    ) {
                        var tiktokOrderModel = tiktokOrderDetailResponse.getData().getOrderList().get(0);
                        TiktokShipPackageRequest shipPackageRequest = new TiktokShipPackageRequest();
                        shipPackageRequest.setPackageId(tiktokOrderModel.getPackageList().get(0).getPackageId());
                        shipPackageRequest.setPickUpType(type);
                        if (type == 1) {
                            var pickUp = new TiktokPickUp();
                            shipPackageRequest.setPickUp(pickUp);
                        }
                        if (type == 2) {
                            var selfShipment = new TiktokSelfShipment();
                            selfShipment.setShippingProviderId(tiktokOrderModel.getShippingProviderId());
                            selfShipment.setTrackingNumber(tiktokOrderModel.getTrackingNumber());
                            shipPackageRequest.setSelfShipment(selfShipment);
                        }
                        var confirmResponse = tikTokApiService.shipPackage(connection.getAccessToken(), connection.getShopId(), shipPackageRequest);
                        if (confirmResponse != null) {
                            if (confirmResponse.getData() != null
                                && confirmResponse.getData().getFailPackages() != null
                                && confirmResponse.getData().getFailPackages().size() > 0)
                            {
                                StringBuilder errorMessage = new StringBuilder();
                                for (TiktokFailPackagesModel failPackage : confirmResponse.getData().getFailPackages()) {
                                    errorMessage
                                        .append(failPackage.getPackageId())
                                        .append(" - ")
                                        .append(failPackage.getFailReason());
                                }
                                response.setError(errorMessage.toString());
                            }
                        } else {
                            response.setError("Có lỗi xảy ra khi xác nhận đơn hàng");
                        }
                    } else {
                        response.setError("Có lỗi xảy ra khi lấy thông ti đơn hàng");
                    }
                }
            }
        } catch (Exception e) {
            response.setError(e.toString());
        }
        return response;
    }

    public BaseResponse getOrderHistory (int orderId) {
        var response = new BaseResponse();
        var histories = new ArrayList<>();

        var channelOrderOptional= channelOrderRepository.findById(orderId);
        if (channelOrderOptional.isPresent()) {
            var channelOrder = channelOrderOptional.get();
            var connectionOptional = connectionRepository.findById(channelOrder.getConnectionId());
            if (connectionOptional.isPresent()) {
                var connection = connectionOptional.get();
                var tiktokHistoryResponse = tikTokApiService.getShippingInfo(connection.getAccessToken(), connection.getShopId(), channelOrder.getOrderNumber());
                if (tiktokHistoryResponse != null
                    && tiktokHistoryResponse.getData() != null
                    && tiktokHistoryResponse.getData().getTrackingInfoList() != null
                ) {
                    tiktokHistoryResponse.getData().getTrackingInfoList().forEach(item -> {
                        histories.addAll(item.getTrackingInfoItems());
                    });
                }
            }
        }
        response.setData(histories);
        return response;
    }

    public BaseResponse printOrderReport (
        List<Integer> connectionIds,
        long from,
        long to
    ) {
        var response = new BaseResponse();
        var connections = connectionRepository.findAllByIdIn(connectionIds);
        if (connections != null) {
            var data = channelOrderRepository.printOrderReport(connectionIds.stream().map(String::valueOf).collect(
                    Collectors.joining(",")), Utils.getDateKey(from), Utils.getDateKey(to));
            List<PrintReportOrderData> printData = new ArrayList<>();
            data.forEach(item  -> {
                var dataRes = new PrintReportOrderData();
                dataRes.setDateKey(Utils.getStringDateFromDateKey(item.getDateKey()));
                dataRes.setTotal(item.getTotal());
                dataRes.setTotalCancelled(item.getTotalCancelled());
                dataRes.setRevenue(item.getRevenue());
                printData.add(dataRes);
            });
            StringBuilder connectionNames = new StringBuilder();
            for (Connection connection : connections) {
                connectionNames.append(connection.getName()).append(",");
            }
            response.setData(Utils.getReportHtml(printData, connectionNames.toString(), String.valueOf(from), String.valueOf(to)));
        }
        return response;
    }

}
