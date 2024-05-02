package com.assets.demo.dto;

import com.assets.demo.models.Position;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HomeDTO {

    private String name;
    private String username;
    private Position position;

}
