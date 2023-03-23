package com.example.demo.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class AppInfoData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String appName;

    private String author;

    private String version;

}
