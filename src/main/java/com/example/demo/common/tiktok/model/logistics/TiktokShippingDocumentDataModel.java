package com.example.demo.common.tiktok.model.logistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokShippingDocumentDataModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("doc_url")
    private String docUrl;

}
