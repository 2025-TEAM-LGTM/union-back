package com.union.demo.utill;

import com.union.demo.enums.PersonalityKey;
import java.util.EnumMap;
import java.util.Map;

//string으로 들어오는 personality를 jsonb로 parser
public class PersonalityParserUtil {
    private PersonalityParserUtil(){
    }

    public static Map<PersonalityKey, Integer> parse(String personality){
        if(personality==null || personality.isBlank()){
            return null;
        }

        Map<PersonalityKey, Integer> res= new EnumMap<>(PersonalityKey.class);

        String [] pers=personality.split(",");

        for(String per : pers){
            String p=per.trim();
            if(p.isEmpty()) continue;

            //키(A,D) 가 들어온 것만 1을 넣어서 처리
            PersonalityKey key=PersonalityKey.valueOf(p);
            res.put(key,1);
        }
        return res.isEmpty()?null:res;

    }


}
