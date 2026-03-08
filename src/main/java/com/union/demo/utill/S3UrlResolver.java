package com.union.demo.utill;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class S3UrlResolver {
    //image의 url(s3 url)을 반환하는 util

    @Value("${cloud.aws.s3.public-base-url}")
    private String baseUrl;

    public String toUrl(String s3Key){
        if(s3Key==null) return null;
        return baseUrl+"/"+s3Key;
    }

}
