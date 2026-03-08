package com.union.demo.dto.request;

import com.union.demo.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ProfileUpdateReqDto {
    private String imageKey;
    private Long imageSize;

    private String email;

    @Schema(example = "101")
    private Long universityId;

    private Integer entranceYear;

    @Schema(example = "ENROLLED")
    private String status;

    @Schema(example = "[225,226]")
    private List<Integer> hardSkills;

    @Schema(example = """
             { "A": 0,"B": 0,"C": 0,
               "D": 0,"E": 0,"F": 0,
               "G": 0,"H": 0,"I": 0,
               "J": 0,"K": 0,"L": 0,
               "M": 0,"N": 0
              }
            """)
    private Map<String, Integer> personality;


}
