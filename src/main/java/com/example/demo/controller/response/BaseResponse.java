package com.example.demo.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String error;
    private Object data;
    private int total;

}
