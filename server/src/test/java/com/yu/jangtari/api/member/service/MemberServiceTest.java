package com.yu.jangtari.api.member.service;

import com.yu.jangtari.ServiceTest;
import com.yu.jangtari.api.member.domain.Member;
import com.yu.jangtari.api.member.domain.RoleType;
import com.yu.jangtari.api.member.dto.MemberDto;
import com.yu.jangtari.api.member.repository.MemberRepository;
import com.yu.jangtari.api.member.repository.RefreshTokenRepository;
import com.yu.jangtari.exception.BusinessException;
import com.yu.jangtari.exception.ErrorCode;
import com.yu.jangtari.security.jwt.JwtInfo;
import com.yu.jangtari.util.GoogleDriveUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MemberServiceTest extends ServiceTest
{
    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private GoogleDriveUtil googleDriveUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private Member admin;
    private Member user;

    @BeforeEach
    void setUp() {
        admin = Member.builder()
            .id(1L)
            .username("username")
            .nickname("nickname")
            .introduce("introduce")
            .password("password").picture("picture").build();
        user = Member.builder()
            .id(2L)
            .username("username")
            .nickname("nickname")
            .introduce("introduce")
            .password("password").picture("picture").build();
    }

    @Test
    @DisplayName("이름으로 member 찾아왔을 때 없으면 NoSuchMemberException 발생")
    void getMemberByName() {
        // given
        // when
        given(memberRepository.findByUsername(any())).willReturn(Optional.empty());

        // then
        BusinessException e = assertThrows(BusinessException.class, () -> memberService.getMemberByName("jang"));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND_ERROR);
    }

    @Test
    @DisplayName("member의 정보 업데이트가 정상적으로 진행됨")
    void updateMember1()
    {
        // given
        MemberDto.Update memDTO = MemberDto.Update.builder().nickname("newNick").introduce("newIntro").build();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(admin));
        given(googleDriveUtil.fileToURL(any(), any())).willReturn("newPic");

        // when
        Member updatedMember = memberService.updateMember(memDTO);

        // then
        assertEquals("newNick", updatedMember.getNickname());
        assertEquals("newIntro", updatedMember.getIntroduce());
        assertEquals("newPic", updatedMember.getPicture());
    }

    @Test
    @DisplayName("1번 회원 Jangtari는 삭제 불가능")
    void deleteMember_X()
    {
        // given
        // when
        // then
        BusinessException e = assertThrows(BusinessException.class, () -> memberService.deleteMember(1L));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND_ERROR);
    }

    @Test
    @DisplayName("member를 정상적으로 삭제")
    void deleteMember()
    {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        Member deletedMember = memberService.deleteMember(user.getId());

        // then
        assertTrue(deletedMember.getDeleteFlag().isDeleted());
    }

    @Test
    @DisplayName("회원가입이 정상적으로 성공함")
    void join()
    {
        // given
        MemberDto.Add memDTO = MemberDto.Add.builder().username("username").nickname("nickname").password("password").build();
        given(passwordEncoder.encode(anyString())).willReturn("encoded");
        given(memberRepository.save(any())).willReturn(admin);

        // when
        memberService.join(memDTO);

        // then
        verify(memberRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    @DisplayName("이미 가입된 username으로 회원가입 하려하면 BusinessException 발생")
    void join_X()
    {
        // given
        MemberDto.Add memDTO = MemberDto.Add.builder().username("username").nickname("nickname").password("password").build();
        willThrow(new DataIntegrityViolationException("중복")).given(memberRepository).save(any());

        // when
        // then
        BusinessException e = assertThrows(BusinessException.class, () -> memberService.join(memDTO));
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DUPLICATED_MEMBER_ERROR);
    }

    @Test
    @DisplayName("정상적으로 로그아웃 가능")
    void logout_O()
    {
        // given
        JwtInfo jwtInfo = JwtInfo.builder()
            .memberId(1L)
            .username("username")
            .nickName("nick")
            .roleType(RoleType.USER)
            .build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(jwtInfo, null, null));
        doNothing().when(refreshTokenRepository).delete(any());

        // when
        memberService.logout();

        // then
        verify(refreshTokenRepository, times(1)).delete(any());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}