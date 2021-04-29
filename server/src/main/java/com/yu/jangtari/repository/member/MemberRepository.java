package com.yu.jangtari.repository.member;

import com.yu.jangtari.domain.Member;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long> {

    public Optional<Member> findByUsername(String username);
}
