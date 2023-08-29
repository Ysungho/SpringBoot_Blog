package my.blog.bootdev.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import my.blog.bootdev.domain.Article;
import my.blog.bootdev.dto.AddArticleRequest;
import my.blog.bootdev.dto.UpdateArticleRequest;
import my.blog.bootdev.repository.BlogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor//final이 붙거나 @NotNull이 붙은 필드의 상성자 추가
//@RequiredArgsConstructor 애너테이션은 빈을 생성자로 생성하는 애너테이션이다. lombok에서 지원한다.
//@NotNull이 붙은 필드로 생성자를 만들어 준다.

@Service//빈으로 등록
//@Service 애너테이션은 해당 클래스를 빈으로 서블릿 컨테이너에 등록해준다.
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request, String userName) {//블로그 글 추가 메서드
        return blogRepository.save(request.toEntity(userName));
    }
    //save()메서드는 JpaRepository에서 지원하는 저장 메서드로 save()로 AddArticleRequest 클래스에 저장된 값들을 article데이터베이스에 저장한다.
    //toEntity()의 인수로 전달받은 유저의 이름을 반환.

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(long id) {//블로그 글 하나를 조회하는 메서드
        //해당 메서드는 데이터베이스에 저장되어 있는 글의 ID를 이용해 글을 조회함.
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
        /*
        여기서 구현한 findById() 메서드는 JPA에서 제공하는 findById()메서드를 사용해서 ID를 받아 엔티티를 조회하고
        없으면 IllegalArgumentException 예외를 발생시킴
         */
    }

    public void delete(long id) {//해당 메서드는 블로그 글의 id를 받은 뒤 jpa에서 제공하는 deleteById() 메서드를 이용해 데이터베이스에서 데이터를 삭제함.
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        authorizeArticleAuthor(article);
        blogRepository.delete(article);
    }

    @Transactional//트랜잭션 메서드
    //@Transactional 애너테이션은 매칭한 메서드를 하나의 트랜잭션으로 묶는 역할을 함.
    //트랜잭션을 적용하기 위해서 다른 작업할 필요 없이 해당 애너테이션만 사용하면 됨.
    //그러면 update() 메서드는 엔티티 필드 값이 바뀌면 중간에 에러가 발생해도 제대로 된 값 수정을 보장하게 되어있음.
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        authorizeArticleAuthor(article);
        article.update(request.getTitle(), request.getContent());

        return article;
    }

    // 게시글을 작성한 유저인지 확인
    private static void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }

}