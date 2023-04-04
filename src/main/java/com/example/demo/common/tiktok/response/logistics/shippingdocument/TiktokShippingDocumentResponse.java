package com.example.demo.common.tiktok.response.logistics.shippingdocument;

import com.example.demo.common.tiktok.model.logistics.TiktokShippingDocumentDataModel;
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
public class TiktokShippingDocumentResponse extends TiktokBaseResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("data")
    private TiktokShippingDocumentDataModel data;

}

