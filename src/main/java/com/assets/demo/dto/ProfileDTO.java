package com.assets.demo.dto;

import lombok.*;

import java.util.List;


@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class ProfileDTO {

    private String username;
    private String name;
    private String surname;
    private String password;
    private List<HomeDTO> homes;

}
