package my.blog.bootdev.controller;

import lombok.RequiredArgsConstructor;

import my.blog.bootdev.domain.Article;
import my.blog.bootdev.dto.ArticleListViewResponse;
import my.blog.bootdev.dto.ArticleViewResponse;
import my.blog.bootdev.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogService blogService;

    @GetMapping("/articles")// /articles GET 요청을 처리할 코드. 여기에서는 블로그 글 전체 리스트를 담은 뷰를 반환함.
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles", articles);//블로그 글 리스트 저장

        return "articleList";//articleList.html라는 뷰 조회
    }

    @GetMapping("/articles/{id}")//블로그 글을 반환할 컨크롤러 메서드
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }
    //getArticle() 메서드는 인자 id에 url로 넘어온 값을 받아 fidnById() 메서드로 넘겨 글을 조회하고,
    //화면에서 사용할 모델에 데이터를 저장한 다음, 보여줄 화면의 템플릿 이름을 반환한다.


    @GetMapping("/new-article")
    //id 키를 가진 쿼리 파라미터의 값을 id 변수에 매핑(단 id는 없을 수도 있음)
    //쿼리 파라미터로 넘어온 id값은 newArticle()메서드의 Long 타입 id인자에 매핑합니다.
    public String newArticle(@RequestParam(required = false) Long id, Model model) {//수정화면ㅇ르 보여주기 위한 컨트롤러 메서드
        if (id == null) {//만약에 id가 없으면 생성,
            // id가 없는 경우 기본 생성자를 이용해 빈 ArticleViewResponse 객체를 만들고 id가 있으면 기존 값을 가져오는 findById()메서드를 호출함.
            model.addAttribute("article", new ArticleViewResponse());
        } else {//id가 없으면 수정, id가 있으면 수정/ 없으면 생성
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }

        return "newArticle";
    }
}
