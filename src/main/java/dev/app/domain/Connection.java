package dev.app.domain;

import dev.app.domain.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

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
