package com.example.demo.controller.response.tiktokorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TikTokOrdersData {

    private List<TikTokOrderData> tikTokOrders;

}
