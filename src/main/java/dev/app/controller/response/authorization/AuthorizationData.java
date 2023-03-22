package dev.app.controller.response.authorization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizationData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;
}
