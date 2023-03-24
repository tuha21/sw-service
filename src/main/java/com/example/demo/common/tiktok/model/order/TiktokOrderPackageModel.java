package com.example.demo.common.tiktok.model.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokOrderPackageModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("package_id")
    private String packageId;
}
