package com.example.demo.model;

import java.math.BigDecimal;

public interface IPrintOrderReport {

    Integer getDateKey();
    Integer getTotal();
    Integer getTotalCancelled();
    BigDecimal getRevenue();

}
