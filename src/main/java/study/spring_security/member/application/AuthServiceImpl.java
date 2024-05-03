package study.spring_security.member.application;

import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.spring_security.member.domain.Member;
import study.spring_security.member.dto.LoginResponseDTO;
import study.spring_security.member.infrastructure.MemberRepository;
import study.spring_security.member.vo.LoginRequestVO;
import study.spring_security.member.vo.req.SignUpRequestVO;
import study.spring_security.security.JwtTokenProvider;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

	private final MemberRepository memberRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void signUp(SignUpRequestVO signUpRequestVO) {
		UUID uuid = UUID.randomUUID();

		memberRepository.save(Member.builder()
				.name(signUpRequestVO.getName())
				.loginId(signUpRequestVO.getLoginId())
				.password(new BCryptPasswordEncoder().encode(signUpRequestVO.getPassword()))
				.uuid(uuid.toString())
				.build());
	}

	@Override
	public LoginResponseDTO logIn(LoginRequestVO loginRequestVO) {
		Member member = memberRepository.findByLoginId(loginRequestVO.getLoginId())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다"));

		log.info("member: {}", member.getLoginId());

		// UUID와 패스워드를 이용하여 인증을 수행
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						member.getUuid(),
						loginRequestVO.getPassword()
				)
		);

		return LoginResponseDTO.builder()
				.accessToken(jwtTokenProvider.generateToken(member))
				.build();
	}
}
