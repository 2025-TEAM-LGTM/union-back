package com.union.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DropdownSkillResDto {

    private Long skillId;
    private String skillName;
    private Long fieldId;
    private String fieldName;
}
