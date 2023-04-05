package com.example.demo.common.tiktok.response.fulfillment;

import com.example.demo.common.tiktok.model.fulfillment.TiktokShipPackageModel;
import com.example.demo.common.tiktok.response.TiktokBaseResponse;
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
public class TiktokShipPackageReponse extends TiktokBaseResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("data")
    private TiktokShipPackageModel data;

}
