package my.blog.bootdev.config.jwt;

import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.Jwts;
import my.blog.bootdev.domain.User;
import my.blog.bootdev.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest
class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")//generateToken()검증 테스트
    //generateToken()메서드는 토큰을 생성하는 메서드를 테스트하는 메서드.
    @Test
    void generateToken() {
        // given: 토큰에 유저 정보를 추가하기 위한 테스트 유져 생성
        User testUser = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        // when: 토큰 제공자의 generateToken()메서드를 호출해 토큰을 생성
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        // then: jjwt 라이브러리를 사용해 토큰을 복호화 합니다.
        // 토큰을 만들 때 클레임으로 넣어둔 id 값이  given절에서 만든 유저 id와 동일한지 확인합니다.
        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(testUser.getId());
    }

    @DisplayName("validToken(): 만료된 토큰인 경우에 유효성 검증에 실패한다.")//validToken()검증 테스트
    //validToken_invalidToken() 메서드는 토큰이 유효한지 검증하는 메서드인 validToken()메서드를 테스트하는 메서드.
    //검증 실패를 확인하는 validToken_invalidToken메서드와 검증 성공을 확인하는 validToken_validToken()메서드가 존재.
    @Test
    void validToken_invalidToken() {
        // given: jjwt 라이브러리를 사용해 토큰을 생성합니다.
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        // when: 토큰 제공지의 validToken()메서드를 호출해 유효한 토큰인지 검증한 뒤 결괏값을 반환받습니다.
        boolean result = tokenProvider.validToken(token);

        // then: 반환값이 false(유효한 토큰이 아님)인 것을 확인합니다.
        assertThat(result).isFalse();
    }


    @DisplayName("validToken(): 유효한 토큰인 경우에 유효성 검증에 성공한다.")
    @Test
    void validToken_validToken() {
        // given: jjwt 라이브러리를 사용해 토큰을 생성합니다. 만료 시간은 현재 시간으로부터 14일 뒤로, 만료되지 않은 토큰을 생성합니다.
        String token = JwtFactory.withDefaultValues()
                .createToken(jwtProperties);

        // when: 토큰 제공자의 validToken() 메서드를 호출해 유효한 토큰인지 검증한 뒤 결괏값을 반환합니다.
        boolean result = tokenProvider.validToken(token);

        // then: 반환값이 true(유효한토큰)인 것을 확인합니다.
        assertThat(result).isTrue();
    }


    @DisplayName("getAuthentication(): 토큰 기반으로 인증정보를 가져올 수 있다.")//getAuthentication() 검증 테스트
    //getAuthentication()메서드는 토큰을 전달받아 인증 정보를 담은 객체 Authentication를 반환하는 메서드인 getQuthentication()을 테스트
    @Test
    void getAuthentication() {
        // given: jjwt 라이브러리를 사용해서 토큰을 생성합니다.
        String userEmail = "user@email.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        // when: 토큰 제공자의 getAuthentication() 메서드를 사용해 인증 객체를 반환 받습니다.
        Authentication authentication = tokenProvider.getAuthentication(token);

        // then: 반환받은 인증 객체의 유저 이름을 가져와 given 절에서 설정한 subject값인 user@email.com 과 같은지 확인합니다.
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    @DisplayName("getUserId(): 토큰으로 유저 ID를 가져올 수 있다.")//getUserID()검증 테스트
    //getUserId()메서드는 토큰 기반으로 유저 ID를 가져오는 메서드를 테스트 하는 메서드임.
    //토큰을 프로퍼티즈 파일에 저장한 비밀값으로 복호화한 뒤 클레임ㅇ르 가져오는 private 메서드인 getClaims()를 호출해서,
    //클레임 정보를 반환받아 클레임에서 id 키로 저장된 값을 가져와 반환함.
    @Test
    void getUserId() {
        // given: jjwt 라이브러리를 사용해 토큰을 생성합니다.
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        // when: 토큰 제공자의 getUserId() 메서드를 호출해 유저 ID를 반환받습니다.
        Long userIdByToken = tokenProvider.getUserId(token);

        // then: 반환받은 ID가 given절에서 설정한 유저 id 값인 1과 같은지 확인합니다.
        assertThat(userIdByToken).isEqualTo(userId);
    }
}