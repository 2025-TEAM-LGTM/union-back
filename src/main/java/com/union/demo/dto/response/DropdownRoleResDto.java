package com.union.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DropdownRoleResDto {
    private Long roleId;
    private String roleName;
    private Long fieldId;
    private String fieldName;
}
