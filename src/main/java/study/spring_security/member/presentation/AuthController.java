package study.spring_security.member.presentation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import study.spring_security.member.application.AuthService;
import study.spring_security.member.dto.LoginResponseDTO;
import study.spring_security.member.vo.LoginRequestVO;
import study.spring_security.member.vo.req.SignUpRequestVO;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping
	public void signUp(@RequestBody SignUpRequestVO signUpRequestVO) {
		authService.signUp(signUpRequestVO);
	}

	@PostMapping("/login")
	public LoginResponseDTO logIn(@RequestBody LoginRequestVO loginRequestVO) {
		return authService.logIn(loginRequestVO);
	}
}
