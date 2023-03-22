package dev.app.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String error;
    private Object data;

}
