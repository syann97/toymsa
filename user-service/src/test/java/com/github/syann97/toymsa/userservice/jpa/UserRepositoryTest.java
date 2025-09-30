package com.github.syann97.toymsa.userservice.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest // 스프링 부트 환경 전체를 로드하여 테스트
@Transactional // 테스트 후 DB 변경 사항 자동 롤백 (선택적)
public class UserRepositoryTest {

	@Autowired
	UserRepository userRepository; // DB 연동을 담당하는 리포지토리 주입

	@Test
	void 데이터베이스_연결_및_삽입_테스트() {
		// Given: 테스트용 엔티티 생성
		UserEntity user = new UserEntity();
		user.setEmail("test_conn@example.com");
		user.setName("Connection Tester");
		user.setUserId("test_conn_user_id");
		user.setEncryptedPassword("test_password_hash");
		// user.setCreatedAt(new Date()); // @CreationTimestamp 또는 DB default로 처리될 경우 생략 가능

		// When: 저장 (DB에 INSERT 시도)
		UserEntity savedUser = userRepository.save(user);

		// Then: 저장된 엔티티가 null이 아니고 ID가 할당되었는지 확인
		assertThat(savedUser).isNotNull();
		assertThat(savedUser.getId()).isNotNull();

		// When: 조회 (DB에서 SELECT 시도)
		Optional<UserEntity> foundUser = userRepository.findById(savedUser.getId());

		// Then: 조회된 사용자가 존재하는지 확인
		assertTrue(foundUser.isPresent());
		assertThat(foundUser.get().getEmail()).isEqualTo("test_conn@example.com");
	}
}