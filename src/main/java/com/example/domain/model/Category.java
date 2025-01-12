package com.example.domain.model;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;
import org.infinispan.protostream.annotations.ProtoTypeId;
import org.hibernate.annotations.Cache;

import jakarta.persistence.*;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@ProtoTypeId(2)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ProtoField(number = 1)
    Long id;

    @ProtoField(number = 2)
    String name;

    @ProtoFactory
    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}