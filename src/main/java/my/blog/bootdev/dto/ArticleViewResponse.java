package my.blog.bootdev.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import my.blog.bootdev.domain.Article;


import java.time.LocalDateTime;

//뷰에서 사용할 dto

@NoArgsConstructor
@Getter
public class ArticleViewResponse {

  private Long id;
  private String title;
  private String content;
  private LocalDateTime createdAt;
  private String author;

  public ArticleViewResponse(Article article) {
    this.id = article.getId();
    this.title = article.getTitle();
    this.content = article.getContent();
    this.createdAt = article.getCreatedAt();
    this.author = article.getAuthor();
  }
}