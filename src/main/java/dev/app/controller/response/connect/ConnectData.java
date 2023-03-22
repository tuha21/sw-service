package dev.app.controller.response.connect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String url;
}
