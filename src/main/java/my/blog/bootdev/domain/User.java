package my.blog.bootdev.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// 회원 엔티티와 매핑할 테이블 구조
// 칼럼명-자료형-null허용 여부- 키- 설명
// id-BIGINT-N-기본키-일련번호, 기본키
// email-VARCHAR(255)-N-X-이메일
// password-VARCHAR(255)-N-X-패스워드(암호화 후 저장)
// created_at-DATETIME-N-X-생성일자
// updated_at-DATETIME-N-X-수정 일자. 


//userDetail을 상속하는 클래스
//User 클래스가 상속한 UserDetail 클래스는 스프링 시큐리티에서 사용자의 인증 정보를 담아 두는 인터페이스이다.

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails {//UserDetails를 상속받아 인증 객체로 사용

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    //사용자 이름
    @Column(name = "nickname", unique = true)
    private String nickname;

    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public User update(String nickname) {//사용자 이름 변경
        this.nickname = nickname;

        return this;
    }


    
    @Override //권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override//사용자의 id를 반환(고유한 값)
    public String getUsername() {
        return email;
    }

    @Override//사용자의 패스워드 반환
    public String getPassword() {
        return password;
    }

    @Override//계정 만료 여부 반환
    public boolean isAccountNonExpired() {
        return true;
    }//만료되었는지 확인하는 로직, true->만료되지 않음. 

    @Override//계정 잠금 여부 반환
    public boolean isAccountNonLocked() {
        return true;
    }//계정 잠금되었는지 확인하는 로직, true->잠금되지 않음. 

    @Override//패스워드의 만료 여부 반환
    public boolean isCredentialsNonExpired() {
        return true;
    }//패스워드가 만료되었는지 확인하는 로직, true->완료되지 않음. 

    @Override//계정 사용 가능 여부 반환
    public boolean isEnabled() {
        return true;
    }//계정이 사용 가능한지 확인하는 로직, true->사용 가능
}

