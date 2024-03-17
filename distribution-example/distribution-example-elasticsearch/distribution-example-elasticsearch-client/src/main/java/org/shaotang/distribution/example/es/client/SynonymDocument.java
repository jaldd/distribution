package org.shaotang.distribution.example.es.client;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class SynonymDocument {
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Text, analyzer = "ik_synonym")
    private String name;
    @Field(type = FieldType.Text, analyzer = "ik_synonym")
    private String specification;
}
