package study.spring_security.member.vo.req;

import lombok.Getter;

@Getter
public class SignUpRequestVO {
	private String name;
	private String loginId;
	private String password;
}
