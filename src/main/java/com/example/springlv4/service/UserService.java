package com.example.springlv4.service;

import com.example.springlv4.dto.LoginRequestDto;
import com.example.springlv4.dto.SignupRequestDto;
import com.example.springlv4.entity.User;
import com.example.springlv4.entity.UserRoleEnum;
import com.example.springlv4.jwt.JwtUtil;
import com.example.springlv4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.lang.*;
import java.util.regex.Pattern;

@Service
@Validated
//다른 곳에서 파라미터를 검증하기 위해
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    //프록시 객체가 생성되어 자동으로 commit 혹은 rollback을 진행해줌
    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();
//        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        //저장하기전에 인코딩
        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        //userRepository에서 username을 찾아옴
        if (found.isPresent()) {
            //같은 username이 존재하는지 확인해줌
            throw new IllegalArgumentException("사용자가 이미 존재합니다.");
        }

        //사용자 Role 확인
        UserRoleEnum role = UserRoleEnum.USER;
        //userRoleEnum의 role은 USER로 선언
        if(signupRequestDto.isAdmin()){
            //가입시에 requestDto에 isAdmin이 true라면
            if(!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)){
                //가입시 requestDto에서 AdminToken을 가져왔을 때 미리 정해둔 토큰값과 같지 않을 때
                throw new IllegalArgumentException("관리자 권한이 일치하지 않습니다.");
                //예외 처리로 권한불일치 메세지를 return 해줌
            }
            role = UserRoleEnum.ADMIN;
            //예외 처리가 되지 않는 경우, 즉 토큰값이 옳다면 roled은 ADMIN이 됨
        }


        //아이디 정규식 확인
        if (!Pattern.matches("^[a-z0-9]{4,10}$", username)) {
            throw new IllegalArgumentException("아이디는 4자 이상, 10자 이하 알파벳 소문자, 숫자로만 이루어져야 합니다.");
        }


        //비밀번호 정규식 확인
        //위에서 예외 처리가 되지 않았다면, 아이디는 생성 가능
        Optional<User> pw = userRepository.findByUsername(password);// 필요 없는 code?
        //Username에 password를 넣어
        if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,15}", password)) {
            //pattern확인
            throw new IllegalArgumentException("비밀번호는 8자 이상, 15자 이하 알파벳 대/소문자, 숫자, 특수문자 로만 이루어져야 합니다.");
            //형식에 맞지 않으면 예외처리, 메세지 리턴
        }
        password = passwordEncoder.encode(signupRequestDto.getPassword());
        //저장하기전에 인코딩

        User user = new User(username, password, role);
        //모두 생성 가능하다면 users에 새로운 계정을 넣어주고
        userRepository.save(user);
        //새로운 계정을 생성해줌
    }

    @Transactional(readOnly = true)
    //프록시 객체가 생성되어 자동으로 commit 혹은 rollback을 진행해줌
    public void login(LoginRequestDto loginRequestDto,HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();
        //requestDto에서 값들을 가져옴, httpresponse로 jwt토큰 돌려줘야함

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 아이디가 없습니다.")
                //username이 존재하지 않을 때 예외처리
        );

        // 비밀번호 확인
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            //users에서 가져온 비밀번호 값과 주어진 password가 다를 때 예외 처리
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(),user.getRole()));
        //모든 예외가 아닐 때 header에 response를 해준다. jwtUtil에서 만든 토큰을 (users에서 Username을 가져와 create)
    }
}