package com.yu.jangtari.MemberTest;

import com.yu.jangtari.RepositoryTest;
import com.yu.jangtari.domain.Member;
import com.yu.jangtari.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MemberRepositoryTest extends RepositoryTest {
    
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("전체 정보 가지는 인원 생성 성공")
    void createMember_O() {
        List<Member> members = makeMembers();
        Member newMember = memberRepository.save(members.get(0));
        compareMemberObj(members.get(0), newMember, 1L);
    }

    @Test
    @DisplayName("intoduce 정보 안 가지는 인원 생성 성공")
    void createMember_without_introduce_O() {
//        List<Member> members = makeMembers();
//        Member newMember = memberRepository.save(members.get(5));
//        compareMemberObj(members.get(5), newMember, 2L);
        List<Member> members = (List<Member>) memberRepository.findAll();
        System.out.println(members.size());
    }


    void compareMemberObj(Member newMember, Member beforeMember, Long id) {
        assertThat(newMember.getUsername()).isEqualTo(beforeMember.getUsername());
        assertThat(newMember.getNickname()).isEqualTo(beforeMember.getNickname());
        assertThat(newMember.getRole()).isEqualTo(beforeMember.getRole());
        assertThat(newMember.getDeleteFlag()).isEqualTo(beforeMember.getDeleteFlag());
        assertThat(newMember.getCreatedDate()).isNotNull();
        assertThat(newMember.getUpdateDate()).isNotNull();
        assertThat(newMember.getId()).isEqualTo(id);
        assertThat(newMember.getPicture()).isEqualTo(beforeMember.getPicture());
        assertThat(newMember.getIntroduce()).isEqualTo(beforeMember.getIntroduce());
    }

    List<Member> makeMembers() {
        List<Member> members = new ArrayList<>();
        LongStream.range(0, 5).forEach(id -> {
            members.add(Member.builder()
                    .username("username " + id)
                    .nickname("nickname " + id)
                    .introduce("introduce " + id)
                    .password("password " + id)
                    .picture("picture " + id).build());
        });
        // introduce가 없는 5번
        members.add(Member.builder()
                .username("username 5")
                .nickname("nickname 5")
                .password("password 5")
                .picture("picture 5").build());
        // picture이 없는 6번
        members.add(Member.builder()
                .username("username 6")
                .nickname("nickname 6")
                .password("password 6")
                .introduce("introduce 6").build());
        return members;
    }
}
