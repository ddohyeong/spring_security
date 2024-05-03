package study.spring_security.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDTO {
	private String accessToken;
}
