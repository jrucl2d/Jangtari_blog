package com.yu.jangtari.MemberTest;

import com.yu.jangtari.ServiceTest;
import com.yu.jangtari.common.exception.NoSuchMemberException;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.repository.member.MemberRepository;
import com.yu.jangtari.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class MemberServiceTest extends ServiceTest {

    @InjectMocks // @Mock이 붙은 객체를 주입받을 객체 
    private MemberService memberService;

    @Mock // Mock 객체 생성
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("성공 테스트")
    class SuccessTest {
        @Test
        @DisplayName("softDelete 성공")
        void softDelete_O() {
            // given
            Member member = makeMember();
            given(memberRepository.save(any())).willReturn(member);
            given(memberRepository.findById(any())).willReturn(Optional.of(member));
            Member savedMember = memberRepository.save(member);
            assertThat(savedMember.getDeleteFlag().isDeleteFlag()).isFalse();
            // when
            Member afterDeletedMember = memberService.deleteMember(1L);
            // then
            assertThat(afterDeletedMember.getDeleteFlag().isDeleteFlag()).isTrue();
        }
    }

   @Nested
    @DisplayName("실패 테스트")
    class FailureTest {
        @Test
        @DisplayName("softDelete 실패 - 해당 Id의 Member 없음")
        void softDelete_X() {
            // given
            given(memberRepository.findById(any())).willReturn(Optional.empty());
            // when, then
            assertThrows(NoSuchMemberException.class, () -> memberService.deleteMember(1L));
        }
    }

    private Member makeMember() {
        return Member.builder()
                .username("username 1")
                .nickname("nickname 1")
                .introduce("introduce 1")
                .password("password 1")
                .picture("picture 1").build();
    }
}

