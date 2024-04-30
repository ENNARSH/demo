package com.assets.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class ProfileDTO {

    private String username;
    private String name;
    private String surname;
    private String password;
    private List<HomeDTO> homes;

}
