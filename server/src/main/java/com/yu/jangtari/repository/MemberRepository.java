package com.yu.jangtari.repository;

import com.yu.jangtari.domain.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {
}
