package my.blog.bootdev.config.oauth;

import lombok.RequiredArgsConstructor;
import my.blog.bootdev.domain.User;
import my.blog.bootdev.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //리소스 서버에서 보내주는 사용자 정보를 불러오는 loadUser() 메서드를 통해 사용자를 조회함
        //사용자가 users 테이블에 사용자 정보가 있다면 이름을 업데이트하고 없다면 saveOrUpdate() 메서드를 실행해서 users 테이블에 회원 데이터를 추가함.
        OAuth2User user = super.loadUser(userRequest); //요청을 바탕으로 유저 정보를 담은 객체 반환
        saveOrUpdate(user);

        return user;
    }

    //유저가 있으면 업데이트, 없으면 유저 생성
    private User saveOrUpdate(OAuth2User oAuth2User) {
        //saveOrUpdate() 메서드는 사용자가 user 테이블에 있으면 업데이트 하고, 없으면 사용자를 새로 생성해서 데이터베이스에 저장함.
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(name))
                .orElse(User.builder()
                        .email(email)
                        .nickname(name)
                        .build());

        return userRepository.save(user);
    }
}