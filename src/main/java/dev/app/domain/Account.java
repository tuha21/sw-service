package dev.app.domain;

import dev.app.domain.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {

    private int tenantId;

    private String username;

    private String password;

    private String fullName;

}
