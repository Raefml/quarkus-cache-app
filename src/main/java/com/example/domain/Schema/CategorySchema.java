package com.example.domain.Schema;

import com.example.domain.model.Category;
import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.ProtoSchema;

@ProtoSchema(includeClasses = Category.class)
public interface CategorySchema extends GeneratedSchema {
}
