package com.union.demo.controller;

import com.union.demo.dto.response.PresignResDto;
import com.union.demo.global.common.ApiResponse;
import com.union.demo.utill.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/presign")
public class UploadController {
    private final S3Uploader s3Uploader;

    private static final Set<String> ALLOWED_IMAGE_TYPES= Set.of(
            "image/png","image/jpeg","image.jpg"
    );

    @PostMapping
    public ResponseEntity<ApiResponse<PresignResDto>> presign(
            @RequestParam String contentType
    ){
        //image 파일의 type 검증
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("허용되지 않은 이미지 타입입니다: " + contentType);
        }

        S3Uploader.PresignResult result=s3Uploader.createPresignedUrl(contentType);

        PresignResDto data=PresignResDto.builder()
                .key(result.key())
                .presignedUrl(result.presignedUrl())
                .build();

        return ResponseEntity.ok(ApiResponse.ok(data));
    }


}