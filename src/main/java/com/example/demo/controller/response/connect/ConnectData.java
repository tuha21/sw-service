package com.example.demo.controller.response.connect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String url;
}
