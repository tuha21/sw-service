package com.example.demo.domain;

import com.example.demo.domain.base.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {

    private int tenantId;

    private String username;

    private String password;

    private String fullName;

}
