package my.blog.bootdev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


//블로그 글 수정 요청을 받을 dto임.
//글에서 수정해야 하는 내용은 제목과 내용이므로 그에 맞게 제목과 내용 필드를 구성함.
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateArticleRequest {
    private String title;
    private String content;
}
