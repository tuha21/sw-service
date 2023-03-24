package com.example.demo.common.tiktok.model.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokOrderRecipientModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("address_detail")
    private String addressDetail;

    @JsonProperty("address_line_list")
    private List<String> addressLineList;

    @JsonProperty("city")
    private String city;

    @JsonProperty("district")
    private String district;

    @JsonProperty("full_address")
    private String fullAddress;

    @JsonProperty("name")
    private String name;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("region")
    private String region;

    @JsonProperty("region_code")
    private String regionCode;

    @JsonProperty("state")
    private String state;

    @JsonProperty("town")
    private String town;

    @JsonProperty("zipcode")
    private String zipcode;
}
