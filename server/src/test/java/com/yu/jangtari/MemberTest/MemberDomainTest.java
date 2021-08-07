package com.yu.jangtari.MemberTest;

import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.domain.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class MemberDomainTest {

    @Test
    @DisplayName("Member 객체 생성 성공 - 모든 값")
    void memberCreate_O() {
        Member member = Member.builder()
                .username("yuseonggeun")
                .nickname("hahaman")
                .password("1234")
                .introduce("hello, I'm hahaman")
                .picture("aaa.com").build();
        assertThat(member.getUsername()).isEqualTo("yuseonggeun");
        assertThat(member.getNickname()).isEqualTo("hahaman");
        assertThat(member.getPassword()).isEqualTo("1234");
        assertThat(member.getIntroduce()).isEqualTo("hello, I'm hahaman");
        assertThat(member.getPicture()).isEqualTo("aaa.com");
        assertThat(member.getDeleteFlag().isDeleteFlag()).isEqualTo(false);
        assertThat(member.getRole()).isEqualTo(RoleType.USER);
    }
}
