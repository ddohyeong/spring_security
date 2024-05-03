package study.spring_security.common.exception;


import lombok.Getter;
import study.spring_security.common.response.BaseResponseStatus;

@Getter
public class BaseException extends RuntimeException{
    private BaseResponseStatus status;

    public BaseException(BaseResponseStatus status) {
        this.status = status;
    }

}
