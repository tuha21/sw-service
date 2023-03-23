package com.example.demo.domain.base;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class SuperBaseEntity extends Entity {
    public abstract int getId();

    public abstract void setId(int id);
}
