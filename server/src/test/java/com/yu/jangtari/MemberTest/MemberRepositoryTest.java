package com.yu.jangtari.MemberTest;

import com.yu.jangtari.RepositoryTest;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MemberRepositoryTest extends RepositoryTest {
    
    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("성공 테스트")
    class SuccessTest {
        @Test
        @DisplayName("모든 정보 있는 Member save 성공")
        void saveMember_O() {
            // given
            List<Member> members = makeMembers();
            // when
            Member newMember = memberRepository.save(members.get(0));
            // then
            compareMemberObj(members.get(0), newMember);
        }

        @Test
        @DisplayName("intoduce 없는 Member save 성공")
        void saveMember_without_introduce_O() {
            //given
            List<Member> members = makeMembers();
            // when
            Member newMember = memberRepository.save(members.get(1));
            // then
            compareMemberObj(members.get(1), newMember);
        }

        @Test
        @DisplayName("picture 없는 Member save 성공")
        void saveMember_without_picture_O() {
            // given
            List<Member> members = makeMembers();
            // when
            Member newMember = memberRepository.save(members.get(2));
            // then
            compareMemberObj(members.get(2), newMember);
        }
    }

    @Nested
    @DisplayName("실패 테스트")
    class FailureTest {
        @Test
        @DisplayName("save 실패 - username 없는 Member")
        void saveMember_without_username_X() {
            // given
            List<Member> members = makeMembers();
            // Exception thrown when an attempt to insert or update data results in violation of an integrity constraint
            // when, then
            assertThrows(DataIntegrityViolationException.class, () -> memberRepository.save(members.get(3)));
        }
        @Test
        @DisplayName("save 실패 - nickname 없는 Member")
        void saveMember_without_nickname_X() {
            // given
            List<Member> members = makeMembers();
            // when, then
            assertThrows(DataIntegrityViolationException.class, () -> memberRepository.save(members.get(4)));
        }
        @Test
        @DisplayName("save 실패 - password 없는 Member")
        void saveMember_without_password_X() {
            // given
            List<Member> members = makeMembers();
            // when, then
            assertThrows(DataIntegrityViolationException.class, () -> memberRepository.save(members.get(5)));
        }
    }

    void compareMemberObj(Member newMember, Member beforeMember) {
        assertThat(newMember.getUsername()).isEqualTo(beforeMember.getUsername());
        assertThat(newMember.getNickname()).isEqualTo(beforeMember.getNickname());
        assertThat(newMember.getRole()).isEqualTo(beforeMember.getRole());
        assertThat(newMember.getDeleteFlag()).isEqualTo(beforeMember.getDeleteFlag());
        assertThat(newMember.getCreatedDate()).isNotNull();
        assertThat(newMember.getUpdateDate()).isNotNull();
        assertThat(newMember.getPicture()).isEqualTo(beforeMember.getPicture());
        assertThat(newMember.getIntroduce()).isEqualTo(beforeMember.getIntroduce());
    }

    private List<Member> makeMembers() {
        List<Member> members = new ArrayList<>();
        members.add(Member.builder() // 전체 정보 있는 객체
                .username("username 1")
                .nickname("nickname 1")
                .introduce("introduce 1")
                .password("password 1")
                .picture("picture 1").build());
        members.add(Member.builder() // introduce 없는 객체
                .username("username 2")
                .nickname("nickname 2")
                .password("password 2")
                .picture("picture 2").build());
        members.add(Member.builder() // picture 없는 객체
                .username("username 3")
                .nickname("nickname 3")
                .password("password 3")
                .introduce("introduce 3").build());
        members.add(Member.builder() // username 없는 객체
                .nickname("nickname 4")
                .password("password 4")
                .introduce("introduce 4").build());
        members.add(Member.builder() // nickname 없는 객체
                .username("username 5")
                .password("password 5")
                .introduce("introduce 5").build());
        members.add(Member.builder() // password 없는 객체
                .username("username 6")
                .nickname("nickname 6")
                .introduce("introduce 6").build());
        return members;
    }
}
