package com.example.demo.common.tiktok.response.logistics.warehouse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokWarehouseAddress {

    private String city;

    private String contactPerson;

    private String district;

    private String fullAddress;

    private String phone;

    private String regionCode;

    private String state;

    private String town;

    private String zipCode;

}
