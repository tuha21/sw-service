package com.example.demo.common.tiktok.model.product;

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
public class TiktokVideo implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private String id;

    @JsonProperty("duration")
    private float duration;

    @JsonProperty("post_url")
    private String postUrl;

    @JsonProperty("media_type")
    private String mediaType;

}
