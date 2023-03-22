package dev.app.domain;

import dev.app.domain.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tenants")
public class Tenant extends BaseEntity {

    private String alias;
    private String uuid;

}
