package my.blog.bootdev.dto;

import lombok.Getter;
import my.blog.bootdev.domain.Article;

//응답을 위한 DTO
//글은 제목과 내용 구성이므로 해당 필드를 가지는 클래스를 만든 다음, 엔티티를 인수로 받는 생성자를 추가함

@Getter
public class ArticleResponse {

    private final String title;
    private final String content;

    public ArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
