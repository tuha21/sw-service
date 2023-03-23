package com.example.demo.domain;

import com.example.demo.domain.base.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "connections")
public class Connection extends BaseEntity {

    private int tenantId;
    private String name;
    private String shopId;
    private String accessToken;
    private String refreshToken;
    private int accessTokenExpiredAt;
    private int refreshTokenExpiredAt;

}
