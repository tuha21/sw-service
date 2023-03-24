package com.example.demo.controller.response;

import com.example.demo.domain.ChannelProduct;
import com.example.demo.domain.base.ChannelVariant;
import java.util.List;
import lombok.Data;

@Data
public class ChannelProductResponse {

    private ChannelProduct product;
    private List<ChannelVariant> variants;
    private int totalMapping;

}
