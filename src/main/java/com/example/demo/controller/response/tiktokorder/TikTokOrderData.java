package com.example.demo.controller.response.tiktokorder;

import com.example.demo.domain.ChannelOrder;
import com.example.demo.domain.ChannelOrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TikTokOrderData {

    private ChannelOrder tiktokOrder;
    private List<ChannelOrderItem> tiktokOrderItems;

}
