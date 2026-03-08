package com.union.demo.repository;

import com.union.demo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    //imageId로 이미지 찾기
    Optional<Image> findByImageId(long imageId);
}
