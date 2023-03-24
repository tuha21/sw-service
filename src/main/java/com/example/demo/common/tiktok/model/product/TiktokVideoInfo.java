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
public class TiktokVideoInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("main_url")
    private String mainUrl;
    @JsonProperty("backup_url")
    private String backupUrl;
    @JsonProperty("url_expire")
    private int urlExpire;
    @JsonProperty("width")
    private int width;
    @JsonProperty("height")
    private int height;
    @JsonProperty("file_hash")
    private String fileHash;
    @JsonProperty("format")
    private String format;
    @JsonProperty("size")
    private int size;
    @JsonProperty("bitrate")
    private int bitrate;
}
