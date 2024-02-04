package org.jm.apiserver.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jm.apiserver.domain.Member;
import org.jm.apiserver.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class SocialController {

    private final MemberService memberService;

    @GetMapping("/api/member/kakao")
    public String[] getMemberFromKakao(String accessToken) {

        log.info("accessToken :" + accessToken);

        memberService.getKakaoMember(accessToken);

        return new String[]{"AAA", "BBB", "CCC"};
    }
}