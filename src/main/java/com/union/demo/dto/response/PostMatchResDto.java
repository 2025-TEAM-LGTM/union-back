package com.union.demo.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostMatchResDto {
    private List<PostMatchUserDto> results;

}
