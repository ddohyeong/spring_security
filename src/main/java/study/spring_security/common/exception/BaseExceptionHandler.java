package study.spring_security.common.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import study.spring_security.common.response.BaseResponse;
import study.spring_security.common.response.BaseResponseStatus;

@RestControllerAdvice
public class BaseExceptionHandler {

     /*
     * 발생한 예외 처리
        등록한 예외 처리
        런타임 에러
        @Valid 에러
     * */

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<?> BaseError(BaseException e) {
        BaseResponse response = new BaseResponse(e.getStatus());
        return new ResponseEntity<>(response, response.httpStatus());
    }
//    runtime error
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<?> RuntimeError(RuntimeException e) {
        BaseResponse response = new BaseResponse(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, response.httpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> processValidationError(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for(FieldError fieldError : bindingResult.getFieldErrors()){
            System.out.println("fieldError.getDefaultMessage() = " + fieldError.getDefaultMessage());
            builder.append(fieldError.getDefaultMessage());
            builder.append(" / ");
        }
        BaseResponse response = new BaseResponse(e, builder.toString());
        return new ResponseEntity<>(response, response.httpStatus());
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> SignatureError(SignatureException e){
        BaseResponse response = new BaseResponse(BaseResponseStatus.INVALID_JWT_SIGNATURE);
        return new ResponseEntity<>(response, response.httpStatus());
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<?> MalformedJwtError(MalformedJwtException e){
        BaseResponse response = new BaseResponse(BaseResponseStatus.INVALID_JWT_MALFORMED_EXCEPTION);
        return new ResponseEntity<>(response, response.httpStatus());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> ExpiredJwtError(ExpiredJwtException e){
        BaseResponse response = new BaseResponse(BaseResponseStatus.INVALID_JWT_EXPIRED);
        return new ResponseEntity<>(response, response.httpStatus());
    }
}
