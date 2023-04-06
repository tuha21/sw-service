package com.example.demo.controller.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelProductsResponse {

    List<ChannelProductResponse> products;

    int total;


}
