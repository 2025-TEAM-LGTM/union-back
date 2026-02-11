package com.union.demo.dto.request;

import com.union.demo.enums.Gender;
import com.union.demo.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupReqDto {
    //users
    @NotBlank
    @Size(max = 50)
    private String loginId;

    @NotBlank
    @Size(max = 255)
    private String password; //암호화 안한 상태. 암호화는 service에서 처리

    @NotBlank
    @Size(max = 50)
    private String username;
    private Long mainRoleId;

    private Map<String, Integer> personality; //성격 정보 추가

    //user_profile
    private String email;
    private Integer birthYear;
    private Gender gender;
    private Long universityId;
    private String major;
    private Integer entranceYear;
    private Status status;


}
