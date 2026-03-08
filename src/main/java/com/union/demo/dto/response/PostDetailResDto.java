package com.union.demo.dto.response;

import com.union.demo.entity.Post;
import com.union.demo.entity.PostInfo;
import com.union.demo.enums.TeamCultureKey;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostDetailResDto {
    private Long postId;
}
