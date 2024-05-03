package study.spring_security.common.exception;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import study.spring_security.common.response.BaseResponse;

@Component
public class BaseExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (BaseException e) {
            setErrorResponse(response, e);
        }

    }

    private void setErrorResponse(HttpServletResponse response,
                                  BaseException be) {
        // 직렬화 하기위한 object mapper
        ObjectMapper objectMapper = new ObjectMapper();
        // response의 contentType, 인코딩, 응답값을 정함
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        BaseResponse baseResponse = new BaseResponse(be.getStatus());
        // BaseResponse를 return 하도록 설정
        try {
            response.getWriter().write(objectMapper.writeValueAsString(baseResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
