package my.blog.bootdev.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//코드는 @NoArgsConstructor 애너테이션을 선언해 접근 제어자가 protected인 기본 생성자를 별도의 코드 없이 생성했다.
//@Getter 애너테이션으로 클래스 필드에 대해 별도 코드 없이 생성자 메서드를 만들 수 있게 만들었다.
//즉 lombok의 애너테이션을 사용해서 코드를 반복해 입력할 필요가 없어 가독성을 더 좋게 만들었다.
@Getter
@Entity//엔티티로 지정
public class Article {

    @Id//id 필드를 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)//기본키 자동으로 1씩 증가
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)//'title' 이라는 not null컬럼과 매칭
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "author", nullable = false)
    private String author;

    @CreatedDate//엔티티가 생성될 때 생성 시간 저장
    //@CreatedDate 애너테이션을 사용하면 엔티티가 생성될 때 생성시간을 created_at 컬럼에 저장함.
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate//엔티티가 수정될 때 수정 시간 저장
    //@LastModifiedDate 애너테이션을 사용하면 엔티티가 수정될 때 마지막으로 수정된 시간을 updated_at 컬럼에 저장함.
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder//빌더 패턴으로 객체 생성
    //@Builder 애너테이션은 lombok에서 지원하는 에너테이션으로 생성자 위에 입력하면 빌더 패턴방식으로 객체를 생성할 수 있다.
    //빌더 패턴을 사용하면 객체를 유연하고 직관적으로 생성할수 있으며, 어떤 필드에 어떤 값이 들어가는지 명시적으로 파악할 수 있다.
    public Article(String author, String title, String content) {//기본 생성자
        this.author = author;
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {//엔티티에 요청받은 값을 수정하는 메서드임.
        this.title = title;
        this.content = content;
    }
}

/*
* 엔티티 구성
* 칼럼명-자료형-null허용여부-키-설명
* id -BIGINT-N-기본키-일련번호, 기본키
* title-VARCHAR(255)-N-X-게시물의 제목
* contnet-VARCHAR(255)-N-X-내용
* */