package com.union.demo.repository;

import com.union.demo.entity.Users;
import com.union.demo.enums.PersonalityKey;

import java.util.List;
import java.util.Map;

public interface MemberRepositoryCustom {
    //멤버 쿼리에 따라 search
    List<Users> searchMembers(
            List<Long> baseUserIds,
            List<Integer> roleIds, List<Integer> hardSkillIds,
            Map<PersonalityKey, Integer> personality);

}
