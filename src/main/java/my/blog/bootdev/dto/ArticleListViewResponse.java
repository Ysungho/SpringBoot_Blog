package my.blog.bootdev.dto;

import lombok.Getter;
import my.blog.bootdev.domain.Article;


//뷰에게 데이터를 전달하기 위한 객체파일
@Getter
public class ArticleListViewResponse {

    private final Long id;
    private final String title;
    private final String content;

    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
