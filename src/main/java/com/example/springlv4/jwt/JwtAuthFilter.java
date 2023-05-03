package com.example.springlv4.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.springlv4.dto.SecurityExceptionDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    //request: 토큰을 가져온다
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.resolveToken(request);

        //로그인, 회원가입은 인증이 필요x, Token 이 Header에 없음
        //인증이 필요없는 URL 는 if 문이 수행되지 않고, 다음 Filter 로 이동
        if(token != null) {
            //validateToken() 메소드를 실행해서 false 가 된다면,
            if(!jwtUtil.validateToken(token)){
                //jwtExceptionHandler 를 실행
                jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value());
                return;
            }
            //Token 에 문제가 없을 시
            //getUserInfoFromToken: Token 에서 UserInfo 유저의 정보를 가져옴
            Claims info = jwtUtil.getUserInfoFromToken(token);
            setAuthentication(info.getSubject());
        }
        filterChain.doFilter(request,response);
    }

    // 인증 객체 생성 및 등록
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        //SecurityContext context 안에 인증한 객체가 들어가게 됨
        Authentication authentication = jwtUtil.createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    //위의 if(token != null)에서 Token 에 대한 오류가 발생했을 때, Exception 한 결과값을 Client 에게 넘김
    //validateToken() 메소드에서 false라면, jwtExceptionHandler() 실행 -> Client 로 반환
    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            //ObjectMapper 를 통해서 변환한다
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


}
