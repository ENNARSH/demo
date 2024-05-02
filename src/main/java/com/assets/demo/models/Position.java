package com.assets.demo.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Position {

    private Long x;
    private Long y;
    private Long z;

}
