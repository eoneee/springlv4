package com.example.springlv4.config;


//import com.example.springlv4.dto.CustomAccessDeniedHandler;
//import com.example.springlv4.dto.CustomAuthenticationEntryPoint;
import com.example.springlv4.jwt.JwtAuthFilter;
import com.example.springlv4.jwt.JwtUtil;
import com.example.springlv4.security.CustomSecurityFilter;
import com.example.springlv4.security.UserDetailsImpl;
import com.example.springlv4.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 스프링 Security 지원
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class WebSecurityConfig {
//Spring Security를 설정하는 파일

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService; //@RequiredArgsConstructor을 해주지 않으면 에러남
//    private final CustomAccessDeniedHandler customAccessDeniedHandler;
//    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    @Bean // 비밀번호 암호화 기능 등록
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    //아래 filter전에 먼저 securityCustomizer걸림
    //Sprng Security를 적용하지 않을 리소스 설정
    public WebSecurityCustomizer webSecurityCustomizer() {
        //ignore():이러한 경로도 들어온 것들은 인증 처리 하는 것을 무시
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                // h2-console 사용 및 resources 접근 허용 설정
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
                // 스프링부트에서 제공하는 정적 리소스 무시 기능 사용
                // 정적 리소스란? 프로그램 실행시 변경되는 정보가 거의 없는 자원, 웹사이트 로고 html 소스 등
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 : Cross-Site Request Forgery
        //쿠키 기반 인증 방식일 때 사용되는 자신도 모르게 공격자에 의도에 따라 서버를 공격하게 되는 것
        http.csrf().disable();
        //Csrf토큰 방식을 사용하지 않음 -> REST API 방식엔 쿠키를 사용하지 않기 때문

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정 → form login이 안되게 함
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //세션 관리 설정 : 어떤 생성 전략일지 설정 -> STATELESS : JWT토큰 방식을 사용할 때 씀



        //permitAll()로 이런 URL을 인증하지 않고 실행 가능하게 함
        http.authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/posts").permitAll()
                .antMatchers(HttpMethod.GET,"/api/post/**").permitAll() //post/{id}에서 get메서드만 허용
                .anyRequest().authenticated()
                //외의 URL요청들을 전부다 authentication(인증처리)
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil),UsernamePasswordAuthenticationFilter.class);
                //JWT인증/인가를 사용하기 위한 설정
                //addFilterBefore : 뒤 Filter이전에 앞 필터 걸리도록 함
                //앞 필터 : JwtAuthFilter 를 통해 인증 객체를 만들어 context 에 추가, 인증 완료
                //뒤 필터 : UsernamePasswordAuthenticationFilter -> 다음 Filter -> Controller 까지도 이동



        //접근 제한 페이지 이동 설정
        http.exceptionHandling().accessDeniedPage("/api/auth/forbidden");


        // 401 Error 처리, Authorization 즉, 인증과정에서 실패할 시 처리
        //http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);

        // 403 Error 처리, 인증과는 별개로 추가적인 권한이 충족되지 않는 경우
        //http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);

        return http.build();
    }


}