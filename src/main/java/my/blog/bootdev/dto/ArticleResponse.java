package my.blog.bootdev.dto;

import lombok.Getter;
import my.blog.bootdev.domain.Article;


@Getter
public class ArticleResponse {

    private final String title;
    private final String content;

    public ArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
