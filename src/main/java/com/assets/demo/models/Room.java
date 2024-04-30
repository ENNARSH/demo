package com.assets.demo.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder
@Document(indexName = "room")
public class Room {

    @Id
    private String id;
    private String name;


}
