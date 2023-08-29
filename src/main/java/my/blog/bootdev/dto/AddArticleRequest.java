package my.blog.bootdev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.blog.bootdev.domain.Article;

//서비스 계층에서 요청을 받을 객체인 AddArticleRequest 임.
//dto 패키지를 컨트롤러에서 요청 본문을 받을 객체은 AddArticleRequest 파일이다.
//dto 는 계층끼리 데이터를 교환하기 위해서 사용하기 위해 사용되는 객체이다.
//dao는 데이터베이스와 연결되고 데이터를 조회하고 수정하는데 사용하는 객체라 데이터 수정과 관련된 로직이 포함되지만,
//dto는 단순하게 데이터를 옮기기 위해 사용하느 ㄴ전달자 역할을 하는 객체이기 때문에 비지니스 로직을 포함하지 않는다.

@NoArgsConstructor//기본 생성자 추가
@AllArgsConstructor//모든 필드 값을 파라미터로 받는 생성자 추가
@Getter
public class AddArticleRequest {
    private String title;

    private String content;

    public Article toEntity(String author) {//생성자를 사용해 객체 생성
        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}

//toEntity()는 빌더 패턴을 사용해 dto를 엔티티로 만들어 주는 메서드 이다.
//해당 메서드는 나중에 블로그에 글을 추가할 때 저장할 엔티티로 변환하는 용도로 사용한다.