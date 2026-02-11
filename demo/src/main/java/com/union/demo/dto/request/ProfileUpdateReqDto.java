package com.union.demo.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ProfileUpdateReqDto {
    private String profileImageUrl;
    private String email;
    private Long universityId;
    private Integer entranceYear;
    private String status;
    private List<Integer> hardSkills;
    private Map<String, Integer> personality;


}
