package com.union.demo.dto.request;

import com.union.demo.enums.Gender;
import com.union.demo.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(example = "401")
    private Long mainRoleId;

    @Schema(example = """
             { "A": 0,"B": 0,"C": 0,
               "D": 0,"E": 0,"F": 0,
               "G": 0,"H": 0,"I": 0,
               "J": 0,"K": 0,"L": 0,
               "M": 0,"N": 0
              }
            """)
    private Map<String, Integer> personality; //성격 정보 추가

    //user_profile
    private String email;

    private Integer birthYear;

    @Schema(example ="FEMALE")
    private Gender gender;

    @Schema(example = "101")
    private Long universityId;

    private String major;

    private Integer entranceYear;

    @Schema(example ="ENROLLED")
    private Status status;


}
