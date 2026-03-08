package com.union.demo.dto.response;

import com.union.demo.entity.Applicant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ApplyResDto {
    private Long applyId;
    private Long postId;
    private Long applicantUserId;
    private OffsetDateTime applyDate;

    public static ApplyResDto from(Applicant app){
        return ApplyResDto.builder()
                .applyId(app.getApplyId())
                .postId(app.getPost().getPostId())
                .applicantUserId(app.getUser().getUserId())
                .applyDate(app.getApplyDate())
                .build();
    }
}
