package com.example.demo.controller.response;

import com.example.demo.domain.Product;
import lombok.Data;

import java.util.List;

@Data
public class ProductsResponse {

    private List<Product> products;
    private int total;

}
