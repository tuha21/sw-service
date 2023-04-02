package com.example.demo.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TikTokFilterOrderRequest {

    private List<Integer> connectionIds;
    private int orderStatus;
    private String query;
    private int page;
    private int limit;

}
