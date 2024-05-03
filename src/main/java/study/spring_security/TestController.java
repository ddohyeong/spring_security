package study.spring_security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import study.spring_security.common.exception.BaseException;
import study.spring_security.common.response.BaseResponse;
import study.spring_security.common.response.BaseResponseStatus;

@RestController
@RequestMapping("/test")
public class TestController {
	@GetMapping
	public BaseResponse<Void> test(@RequestHeader("Authorization") String token) {
		System.out.println(token);

		return new BaseResponse<>();
	}
}
