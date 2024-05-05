package study.spring_security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import study.spring_security.common.exception.BaseException;
import study.spring_security.common.response.BaseResponse;
import study.spring_security.common.response.BaseResponseStatus;

@RestController
@RequestMapping("/test")
public class TestController {
	@GetMapping
	public BaseResponse<Void> test(@RequestHeader("Authorization") String token,
			@RequestParam("test") String test) {
		System.out.println(token);
		if (test.equals("error")) {
			throw new BaseException(BaseResponseStatus.WRONG_JWT_TOKEN);
		}
		return new BaseResponse<>();
	}
}
