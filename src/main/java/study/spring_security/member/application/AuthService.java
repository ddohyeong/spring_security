package study.spring_security.member.application;


import study.spring_security.member.dto.LoginResponseDTO;
import study.spring_security.member.vo.LoginRequestVO;
import study.spring_security.member.vo.req.SignUpRequestVO;

public interface AuthService {
	void signUp(SignUpRequestVO signUpRequestVO);

	LoginResponseDTO logIn(LoginRequestVO loginRequestVO);
}
