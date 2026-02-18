package com.union.demo.repository;

import com.union.demo.entity.Users;
import com.union.demo.enums.PersonalityKey;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{
    @PersistenceContext
    private final EntityManager em;

    private final ObjectMapper objectMapper=new ObjectMapper();

    @Override
    public List<Users> searchMembers(
            List<Long> baseUserIds,
            List<Integer> roleIds,
            List<Integer> hardSkillIds,
            Map<PersonalityKey, Integer> personality
    ) {
        boolean hasBase = baseUserIds != null && !baseUserIds.isEmpty();
        boolean hasRole = roleIds != null && !roleIds.isEmpty();
        boolean hasSkill = hardSkillIds != null && !hardSkillIds.isEmpty();
        boolean hasPersonality = personality != null && !personality.isEmpty();

        //아무 필터도 없으면 전체 조회
        if (!hasBase && !hasRole && !hasSkill && !hasPersonality) {
            return em.createQuery("select u from Users u", Users.class)
                    .getResultList();
        }

        //필터링
        StringBuilder sql = new StringBuilder();
        sql.append("""
                select distinct u.*
                from users u
                where 1=1
                """);

        //대상이 되는 유저들 집합 제한(applicants, matching 필터링용)
        if (hasBase) {
            sql.append("\n and u.user_id in (:baseUserIds)");
        }

        //role OR 조건
        if (hasRole) {
            sql.append("\n  and u.main_role_id in (:roleIds)");
        }

        //hardSkill OR 조건: exists -> 유저가 해당 스킬들 중 하나라도 가지면 됨
        if (hasSkill) {
            sql.append("""
                    \n and
                    exists(
                        select 1
                        from user_skill us
                        where us.user_id = u.user_id
                            and us.skill_id in (:hardSkillIds)                  
                    )
                    """);
        }

        //personality OR 조건
        String personalityJson = null;

        if (hasPersonality) {
            //Map<PersonalityEnum, Integer> -> Map<String, Integer> 변환
            //PersonalityKey.A -> "A" 로 변환하는 과정
            Map<String, Integer> p = new LinkedHashMap<>();
            for (var e : personality.entrySet()) {
                p.put(e.getKey().name(), e.getValue());
            }

            //Map 형태를 JSON 문자열로 변환
            //Map의 형태를 -> "{"A" : 1}'
            try {
                personalityJson = objectMapper.writeValueAsString(p);
            } catch (Exception e) {
                throw new IllegalArgumentException("personality json 변환 실패");
            }

            sql.append("\n and u.personality @> cast(:personalityJson as jsonb)");

        }

        var query = em.createNativeQuery(sql.toString(), Users.class);

        if (hasBase) query.setParameter("baseUserIds", baseUserIds);
        if (hasRole) query.setParameter("roleIds", roleIds);
        if (hasSkill) query.setParameter("hardSkillIds", hardSkillIds);
        if (hasPersonality) query.setParameter("personalityJson", personalityJson);

        @SuppressWarnings("unchecked")
        List<Users> res = query.getResultList();
        return res;
    }
}
