package com.example.demo.common.tiktok.model.fulfillment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokFailPackagesModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("fail_code")
    private String failCode;

    @JsonProperty("fail_reason")
    private String failReason;

    @JsonProperty("package_id")
    private String packageId;

}
