package com.assets.demo.models;

import com.assets.demo.dto.HomeDTO;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder
@Document(indexName = "home", createIndex = false)
public class Home {

    @Id
    private String id;
    private String name;
    private String usernameID;
    private Position position;

    public boolean belongsToProfile(HomeDTO homeDTO) {
        return this.usernameID.equals(homeDTO.getUsername());
    }

}
