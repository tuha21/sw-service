package com.example.demo.common.tiktok.request.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiktokOrderDetailsRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("order_id_list")
    private List<String> orderIdList;
}
