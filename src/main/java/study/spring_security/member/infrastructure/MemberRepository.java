package study.spring_security.member.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import study.spring_security.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByUuid(String uuid);

	Optional<Member> findByLoginId(String loginId);
}
