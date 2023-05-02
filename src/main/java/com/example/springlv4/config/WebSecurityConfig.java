package com.example.springlv4.config;


import com.example.springlv4.dto.CustomAccessDeniedHandler;
import com.example.springlv4.dto.CustomAuthenticationEntryPoint;
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
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    //@RequiredArgsConstructor을 해주지 않으면 에러남
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


    @Bean // 비밀번호 암호화 기능 등록
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    //아래 filter전에 먼저 securityCustomizer걸림
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console 사용 및 resources 접근 허용 설정
        //ignore():이러한 경로도 들어온 것들은 인증 처리 하는 것을 무시
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf().disable();

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정 → form login이 안되게 함
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //permitAll()로 이런 URL을 인증하지 않고 실행 가능하게 함
        http.authorizeRequests().antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/posts").permitAll()
                .antMatchers(HttpMethod.GET,"/api/post/**").permitAll() //post/{id}에서 get메서드만 허용
                .anyRequest().authenticated()
                //외의 URL요청들을 전부다 authentication(인증처리) 할 것
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil),UsernamePasswordAuthenticationFilter.class);
                //JWT인증/인가를 사용하기 위한 설정
                //Custom Filter등록
                //addFilterBefore : 어떤 Filter이전에 추가 --> 우리가 만든 JwtAuthFilter 를 UsernamePasswordAuthenticationFilter 이전에 실행할 수 있도록
                //1. JwtAuthFilter 를 통해 인증 객체를 만들고 --> 2. context 에 추가 --> 3.인증 완료
                // --> UsernamePasswordAuthenticationFilter 수행 --> 인증됐으므로 다음 Filter 로 이동 --> Controller 까지도 이동


//        http.formLogin().loginPage("/api/auth/login").permitAll();

        // Custom Filter 등록하기
//        http.addFilterBefore(new CustomSecurityFilter(userDetailsService, passwordEncoder()), UsernamePasswordAuthenticationFilter.class);
        //필터가 뒤필터가 돌기전에 먼저 인증객체 만들고 추가하면 인증 됨 다음 필터로 넘어감

//         접근 제한 페이지 이동 설정
        http.exceptionHandling().accessDeniedPage("/api/auth/forbidden");


        // 401 Error 처리, Authorization 즉, 인증과정에서 실패할 시 처리
//        http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);

        // 403 Error 처리, 인증과는 별개로 추가적인 권한이 충족되지 않는 경우
//        http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);

        return http.build();
    }


}