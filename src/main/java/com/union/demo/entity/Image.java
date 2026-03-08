package com.union.demo.entity;
import com.union.demo.enums.Purpose;
import com.union.demo.enums.SizeType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="image_id")
    private Long imageId;

    @Column(name="s3_key", length = 255)
    private String s3Key;

    @Enumerated(EnumType.STRING)
    @Column(name="purpose", length = 255)
    private Purpose purpose;

    @Column(name="file_size")
    private Long fileSize;

    public void updateS3Key(String s3Key){
        this.s3Key=s3Key;
    }
    public void updateFileSize(Long imageSize){this.fileSize=imageSize;}

    public static Image of(String s3Key, Purpose purpose, Long fileSize){
        Image image=new Image();
        image.s3Key=s3Key;
        image.purpose=purpose;
        image.fileSize=fileSize;
        return image;
    }


}
