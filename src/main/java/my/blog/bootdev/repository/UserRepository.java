package my.blog.bootdev.repository;


import my.blog.bootdev.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);//emil로 사용자 정보를 가져옴
}

//사용자 정보를 가져오기 위해서는 스프링 시큐리티가 이메일을 전달받아야 한다.
//스프링 데이터 JPA는 메서드 규칙에 맞춰 메서드를 선언하며 이름을 분석해 자동으로 쿼리를 생성해준다.