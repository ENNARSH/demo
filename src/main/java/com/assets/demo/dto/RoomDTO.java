package com.assets.demo.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RoomDTO {

    private String name;
    private String homeId;

}
