package com.example.domain.Schema;

import com.example.domain.model.Product;
import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.ProtoSchema;

@ProtoSchema(includeClasses = Product.class)
public interface ProductSchema extends GeneratedSchema {
}