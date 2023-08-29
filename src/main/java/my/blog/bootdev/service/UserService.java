package my.blog.bootdev.service;

import lombok.RequiredArgsConstructor;
import my.blog.bootdev.domain.User;
import my.blog.bootdev.dto.AddUserRequest;
import my.blog.bootdev.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


//
@RequiredArgsConstructor
@Service
public class UserService {
    //AddUserRequest 객체를 인수로 받는 회원 정보를 추가 하는 메서드.
    private final UserRepository userRepository;

    public Long save(AddUserRequest dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))//패스워드 암호화,
                // 패스워드를 저장할 때 시큐리티를 설정하며 패스워드 인코딩용으로 등록한 빈을 사용해서 암호화한 후 저장.
                .build()).getId();
    }

    public User findById(Long userId) {//전달받은 유저id로 유저를 검색해서 전달하는 메서드
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email) {
        //findByEmail()메서드는 이메일을 입력받아 users 테이블에서 유저를 찾고, 없으면 예외를 발생시킨다.
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}