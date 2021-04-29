package com.yu.jangtari;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/*
 * JUnit4의 @Runwith() 대신 JUnit5의 @ExtendWith()를 사용
 * repository 테스트를 위해 @DataJpaTest 사용
 * @DataJpaTest는 기본적으로 메모리 데이터베이스에 대한 테스트, @Transactional 처리도 해줌
 * @AutoConfigureTestDatabase로 profile에 등록된 데이터베이스 사용 가능
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@EnableJpaAuditing
public class RepositoryTest {
}
