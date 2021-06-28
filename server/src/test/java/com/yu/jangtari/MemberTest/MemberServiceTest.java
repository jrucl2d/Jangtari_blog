package com.yu.jangtari.MemberTest;

import com.yu.jangtari.ServiceTest;
import com.yu.jangtari.common.exception.JangtariDeleteError;
import com.yu.jangtari.common.exception.NoSuchMemberException;
import com.yu.jangtari.config.GoogleDriveUtil;
import com.yu.jangtari.domain.DTO.MemberDTO;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.repository.member.MemberRepository;
import com.yu.jangtari.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

public class MemberServiceTest extends ServiceTest {

    @InjectMocks // @Mock이 붙은 객체를 주입받을 객체 
    private MemberService memberService;

    @Mock // Mock 객체 생성
    private MemberRepository memberRepository;
    @Mock
    private GoogleDriveUtil googleDriveUtil;

    @Nested
    @DisplayName("성공 테스트")
    class SuccessTest {
        @Test
        @DisplayName("softDelete 성공")
        void deleteMember_O() {
            // given
            Member member = makeMember();
            given(memberRepository.save(any())).willReturn(member);
            given(memberRepository.findById(any())).willReturn(Optional.of(member));
            Member savedMember = memberRepository.save(member);
            assertThat(savedMember.getDeleteFlag().isDeleteFlag()).isFalse();
            // when
            Member afterDeletedMember = memberService.deleteMember(2L);
            // then
            assertThat(afterDeletedMember.getDeleteFlag().isDeleteFlag()).isTrue();
        }
        @Test
        @DisplayName("updateMember O, 사진 없이 update 성공")
        void updateMember_O() {
            // given
            Member member = makeMember();
            given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
            MemberDTO.Update memberDTO = MemberDTO.Update.builder().nickname("changedNick").introduce("changedIntro").build();
            // when
            Member modifiedMember = memberService.updateMember(memberDTO);
            // then
            assertThat(modifiedMember.getIntroduce()).isEqualTo(memberDTO.getIntroduce());
            assertThat(modifiedMember.getNickname()).isEqualTo(memberDTO.getNickname());
        }
        @Test
        @DisplayName("updateMember O, 사진 있게 update 성공")
        void updateMember_O1() {
            // given
            Member member = makeMember();
            given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
            given(googleDriveUtil.fileToURL(any(), any())).willReturn("changedPicture");
            MockMultipartFile pictureFile = new MockMultipartFile("pic", new byte[] {0,});
            MemberDTO.Update memberDTO = MemberDTO.Update.builder().nickname("changedNick").introduce("changedIntro").picture(pictureFile).build();
            // when
            Member modifiedMember = memberService.updateMember(memberDTO);
            // then
            assertThat(modifiedMember.getIntroduce()).isEqualTo(memberDTO.getIntroduce());
            assertThat(modifiedMember.getNickname()).isEqualTo(memberDTO.getNickname());
            assertThat(modifiedMember.getPicture()).isEqualTo("changedPicture");
        }
    }

   @Nested
    @DisplayName("실패 테스트")
    class FailureTest {
        @Test
        @DisplayName("updateMember X, member가 없으면 실패")
        void updateMember_X() {
            // given
            given(memberRepository.findById(any())).willReturn(Optional.empty());
            // when, then
            assertThrows(NoSuchMemberException.class, () -> memberService.updateMember(MemberDTO.Update.builder().nickname("null").build()));
        }
        @Test
        @DisplayName("deleteMember X - 해당 Id의 Member 없음")
        void deleteMember_X() {
            // given
            given(memberRepository.findById(any())).willReturn(Optional.empty());
            // when, then
            assertThrows(NoSuchMemberException.class, () -> memberService.deleteMember(2L));
        }
        @Test
        @DisplayName("deleteMember X, Id가 1인 Jangtari는 삭제 불가")
        void deleteMember_X1() {
            // given, when, then
            assertThrows(JangtariDeleteError.class, () -> memberService.deleteMember(1L));

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

