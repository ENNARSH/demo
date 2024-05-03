package com.assets.demo.models;

import lombok.Builder;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


@Data
@Builder
@Document(indexName = "profile", createIndex = false)
public class Profile {

    @Id
    private String id;
    private String username;
    private String name;
    private String surname;
    private String password;


}
