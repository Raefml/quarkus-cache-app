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
@ProtoTypeId(1)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ProtoField(number = 1)
    Long id;

    @ProtoField(number = 2)
    String name;

    @ProtoField(number = 3)
    Double price;

    @Column(name = "category_id")
    @ProtoField(number = 4)
    Long categoryId;

    @ProtoFactory
    public Product(Long id, String name, Double price, Long categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
    }

    public Product() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}