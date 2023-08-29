package my.blog.bootdev.service;

import lombok.RequiredArgsConstructor;
import my.blog.bootdev.domain.User;
import my.blog.bootdev.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


//스프링 시큐리티에서 로그인을 진행할 때 사용자 정보를 가져오는 코드
@RequiredArgsConstructor
@Service
//스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override//사용자 이름(email)으로 사용자의 정보를 가져오는 메서드
    //필수로 구현해야 하는 loadUserUsername() 메서드를 오버라이딩해서 사용자 정보를 가져오는 로직.
    public User loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException((email)));
    }
}