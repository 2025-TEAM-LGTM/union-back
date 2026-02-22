package com.union.demo.service;

import com.union.demo.dto.request.PostMatchReqDto;
import com.union.demo.dto.response.MemberMatchResDto;
import com.union.demo.dto.response.PostMatchResDto;
import com.union.demo.dto.response.PostMatchUserDto;
import com.union.demo.entity.Users;
import com.union.demo.enums.PersonalityKey;
import com.union.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostMatchService {

    private final RestTemplate restTemplate;
    private final MemberService memberService;

    public MemberMatchResDto postMatchFastApi(
            Long postId,
            List<Integer> roleIds,
            List<Integer> hardSkillsIds,
            Map<PersonalityKey,Integer> personality

    ) {
        String url = "http://localhost:8000/match_result";
        PostMatchReqDto req = new PostMatchReqDto(postId);

        PostMatchResDto fastRes;
        try {
            fastRes = restTemplate.postForObject(url, req, PostMatchResDto.class);
        } catch (RestClientException e) {
            return MemberMatchResDto.builder()
                    .members(List.of())
                    .build();
        }

        if (fastRes == null || fastRes.getResults() == null || fastRes.getResults().isEmpty()) {
            return MemberMatchResDto.builder()
                    .members(List.of())
                    .build();
        }

        List<PostMatchUserDto> results = fastRes.getResults();

        //fastApi가 준 userId 순서 그대로 유지하도록
        List<Long> matchingUserIds = results.stream()
                .map(PostMatchUserDto::getUserId)
                .filter(Objects::nonNull)
                .toList();

        if (matchingUserIds.isEmpty()) {
            return MemberMatchResDto.builder()
                    .members(List.of())
                    .build();
        }

        //userId -> mainStrength 매핑
        Map<Long, String> strengthMap = results.stream()
                .filter(r -> r.getUserId() != null)
                .collect(Collectors.toMap(
                        PostMatchUserDto::getUserId,
                        PostMatchUserDto::getMainStrength,
                        (a, b) -> a,// 중복 id가 들어오면 첫번째 값으로
                        LinkedHashMap::new
                ));

        return memberService.getMatchMembers(
                matchingUserIds,
                strengthMap,
                roleIds,
                hardSkillsIds,
                personality
        );

    }


}
