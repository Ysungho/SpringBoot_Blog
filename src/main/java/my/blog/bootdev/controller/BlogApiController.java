package my.blog.bootdev.controller;

import lombok.RequiredArgsConstructor;

import my.blog.bootdev.domain.Article;
import my.blog.bootdev.dto.AddArticleRequest;
import my.blog.bootdev.dto.ArticleResponse;
import my.blog.bootdev.dto.UpdateArticleRequest;
import my.blog.bootdev.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


//컨트롤러 메서드에서는 URL매핑 애너테이션 @GetMapping, @PostMappping, @PutMapping, @DeleteMapping 등을 사용할 수 있다.


@RequiredArgsConstructor
@RestController//HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
//@RestController 애너테이션을 클래스에 붙이면 HTTP 응답으로 객체 데이터를 JSON형식으로 반환한다.


public class BlogApiController {

    private final BlogService blogService;

    @PostMapping("/api/articles")//HTTP 메서드가 POST일 때 전달받은 URL와 동일한 메서드로 매핑
    //@PostMapping()애너테이션은 HTTP메서드가 POST일 때 요청받은 URL과 동일한 메서드와 메핑한다.

    //요청 본문 값 매핑
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request, Principal principal) {
        Article savedArticle = blogService.save(request, principal.getName());

        //요청한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송
        //@RequestBody 애너테이션은 HTTP를 요청할 때 응답에 해당하는 값을 @RequestBody 애너테이션이 붙는 대상 객체인 AddArticleRequest에 매핑한다.
        //ResponseEntity.status().body()는 응답코드로 201, 즉 Created를 응답하고 테이블에 저장된 객체를 반환한다.
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {//전체 글을 조회한 뒤 반환하는 메서드
        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(articles);
        /* /api/articles GET 요청이 오면 글 전체를 조회하는 findAll()메서드를 호출한 다음
        응답용 객체인 ArticleResponse로 파싱해 body에 담아 클라이언트에 전송함. */
    }
    @GetMapping("/api/articles/{id}")//URL 경로에서 값 추출
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {//URL에서 {id}에 해당하는 값이 id로 들어옴.
        //@PathVariable 애너테이션은 URL에서 값을 가지고 오는 애너테이션임.
        Article article = blogService.findById(id);

        return ResponseEntity.ok()
                .body(new ArticleResponse(article));


    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {//URL에서 {id}에 해당하는 값이 id로 들어옴.
        blogService.delete(id);

        return ResponseEntity.ok()
                .build();
        /* /api/articles/{id}DELETE 요청이 들어오면 {id}에 해당하는 값이 @PathVariable 애너테이션을 통해서 들어옴*/
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id,
                                                 @RequestBody UpdateArticleRequest request) {
        Article updatedArticle = blogService.update(id, request);

        return ResponseEntity.ok()
                .body(updatedArticle);
    }
    // /api/articles/{id}/PUT 요청이 오면 Request Body 정보가 request로 넘어옴
    //그 다음 서비스 클래스의 update()메서드인 id와 request를 넘겨줌.
    //응답 값은 body에 담아 전송함.
}
