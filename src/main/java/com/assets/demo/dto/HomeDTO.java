package com.assets.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HomeDTO {

    private String name;
    private String username;

}
