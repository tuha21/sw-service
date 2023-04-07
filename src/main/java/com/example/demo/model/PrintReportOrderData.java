package com.example.demo.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrintReportOrderData {

    private String dateKey;
    private Integer total;
    private Integer totalCancelled;
    private BigDecimal revenue;

}
