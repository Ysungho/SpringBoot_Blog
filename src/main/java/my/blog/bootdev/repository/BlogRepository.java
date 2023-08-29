package my.blog.bootdev.repository;


import my.blog.bootdev.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
//jpaRepository 클래스를 상속받을 때 엔티티 Article과 엔티티의 PK타입 Long을 인수로 넣었음.
//이 리포지토리를 사용할 때 JpaRepository에서 제공하는 여러 메서드를 사용할 수 있다.
