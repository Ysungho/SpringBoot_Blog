package my.blog.bootdev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import my.blog.bootdev.domain.Article;
import my.blog.bootdev.domain.User;
import my.blog.bootdev.dto.AddArticleRequest;
import my.blog.bootdev.dto.UpdateArticleRequest;
import my.blog.bootdev.repository.BlogRepository;
import my.blog.bootdev.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


//given: 블로그 글 추가에 필요한 요청 객체를 만듬
//when: 블로그 추가 api에 요청을 보냄. 이 때 요청 타입은 JSON이며, given 절에서 미리 만들어둔 객체를 요청 본문으로 함께 전송한다.
//then: 응답 코드가 201 created인지 확인한다. blog를 전체 조회해 크기가 1인지 확인하고 실제로 저장된 데이터와 요청값을 비교한다.


@SpringBootTest//테스트용 애플리케이션 컨택스트
@AutoConfigureMockMvc//MockMvc 생성
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;//짖렬화, 역직렬화를 위한 클래스
    //ObjectMapper 클래스로 만든 객체는 자바 객체를 JSON 데이터로 변환하는 직렬화 또는
    //반대로 JSON데이터를 자바에서 사용하기 위해 자바 객체로 변환하는 역직렬화 할 때 사용함.
    //참고로 ObjectMapper는 Jackson라이브러리에서 제공함.

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach//테스트 실행 전 실행하는 메서드
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        blogRepository.deleteAll();
    }


    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
    }


    @DisplayName("addArticle: 아티클 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        //given: 블로그 글 추가에 필요한 요청 객체를 만듬
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        //객체 JSON으로 직렬화
        final String requestBody = objectMapper.writeValueAsString(userRequest);
        //writeValueAsString() 메서드를 사용해서 객체를 JSON으로 직렬화 해준다.
        //그 이후 MockMvc를 사용해서 HTTP메서드, URL, 요청본문, 요청 타입 등을 설정한 뒤 설정한 내용을 바탕으로 테스트 요청을 보낸다.

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        //when: 블로그 추가 api에 요청을 보냄. 이 때 요청 타입은 JSON이며, given 절에서 미리 만들어둔 객체를 요청 본문으로 함께 전송한다.
        //설정한 내용을 바탕으로 요청 전송
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));
        //contentType()메서드는 요청을 보낼 때 JSON, XML 등 다양한 타입 중 하나를 골라서 요청을 보낸다.
        //단 해당 코드에서는 JSON 타입의 요청을 보낸다.

        //then: 응답 코드가 201 created인지 확인한다. blog를 전체 조회해 크기가 1인지 확인하고 실제로 저장된 데이터와 요청값을 비교한다.
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);//크기가 1인지 검증
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
        // ##자주 사용하는 assertThat 메서드## //
        //assertThat() 메소드로는 블로그 글의 개수가 1인지 확인한다.
        //assertThat(articles.size()).isEqualTo(1): 블로그 글의 크기가 1이여야 한다.
        //assertThat(articles.size()).isGreaterThan(2): 블로그 글의 크기가 2보다 커야 한다.
        //assertThat(articles.size()).isLessThan(5):블로그 글의 크기가 5보다 작아야 한다.
        //assertThat(articles.size()).isZero(): 블로그 글 크기가 0이여야 한다.
        //assertThat(articles.title()).isEqualTo("제목"): 블로그 title이 "제목" 이여야 한다.
        //assertThat(articles.title()).isNotEmpty(): 블로그 글의 title 값이 비어있지 않아야 한다.
        //assertThat(articles.title()).contains("제"): 블로그 글의 title 값이 "제"를 포함해야 한다.
    }

    @DisplayName("findAllArticles: 아티클 목록 조회에 성공한다.")
    @Test
    public void findAllArticles() throws Exception {
        //given: 블로그 글 추가에 필요한 요청 객체를 만듬
        final String url = "/api/articles";
        Article savedArticle = createDefaultArticle();

        //when: 블로그 추가 api에 요청을 보냄. 이 때 요청 타입은 JSON이며, given 절에서 미리 만들어둔 객체를 요청 본문으로 함께 전송한다.
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        //then: 응답 코드가 201 created인지 확인한다. blog를 전체 조회해 크기가 1인지 확인하고 실제로 저장된 데이터와 요청값을 비교한다.
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(savedArticle.getContent()))
                .andExpect(jsonPath("$[0].title").value(savedArticle.getTitle()));
    }

    @DisplayName("findArticle: 아티클 단건 조회에 성공한다.")
    @Test
    public void findArticle() throws Exception {
        //given: 블로그 글 추가에 필요한 요청 객체를 만듬
        final String url = "/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        //when: 블로그 추가 api에 요청을 보냄. 이 때 요청 타입은 JSON이며, given 절에서 미리 만들어둔 객체를 요청 본문으로 함께 전송한다.
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        //then: 응답 코드가 201 created인지 확인한다. blog를 전체 조회해 크기가 1인지 확인하고 실제로 저장된 데이터와 요청값을 비교한다.
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(savedArticle.getContent()))
                .andExpect(jsonPath("$.title").value(savedArticle.getTitle()));
    }


    @DisplayName("deleteArticle: 아티클 삭제에 성공한다.")
    @Test
    public void deleteArticle() throws Exception {
        //given: 블로그 글 추가에 필요한 요청 객체를 만듬
        final String url = "/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        //when: 블로그 추가 api에 요청을 보냄. 이 때 요청 타입은 JSON이며, given 절에서 미리 만들어둔 객체를 요청 본문으로 함께 전송한다.
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());

        //then: 응답 코드가 201 created인지 확인한다. blog를 전체 조회해 크기가 1인지 확인하고 실제로 저장된 데이터와 요청값을 비교한다.
        List<Article> articles = blogRepository.findAll();

        assertThat(articles).isEmpty();
    }


    @DisplayName("updateArticle: 아티클 수정에 성공한다.")
    @Test
    public void updateArticle() throws Exception {
        //given: 블로그 글 추가에 필요한 요청 객체를 만듬
        final String url = "/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        final String newTitle = "new title";
        final String newContent = "new content";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        //when: 블로그 추가 api에 요청을 보냄. 이 때 요청 타입은 JSON이며, given 절에서 미리 만들어둔 객체를 요청 본문으로 함께 전송한다.
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then: 응답 코드가 201 created인지 확인한다. blog를 전체 조회해 크기가 1인지 확인하고 실제로 저장된 데이터와 요청값을 비교한다.
        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }

    private Article createDefaultArticle() {
        return blogRepository.save(Article.builder()
                .title("title")
                .author(user.getUsername())
                .content("content")
                .build());
    }
}