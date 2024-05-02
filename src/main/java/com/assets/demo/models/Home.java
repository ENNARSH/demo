package com.assets.demo.models;

import com.assets.demo.dto.HomeDTO;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder
@Document(indexName = "home")
public class Home {

    @Id
    private String id;
    private String name;
    private String usernameID; //TODO: se cancello una casa, deve cancellare automaticamente le stanze, completa logica di cancellazione profile
    private Position position;

    public boolean belongsToProfile(HomeDTO homeDTO) {
        return this.usernameID.equals(homeDTO.getUsername());
    }

}
