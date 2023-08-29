package my.blog.bootdev.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import my.blog.bootdev.dto.AddUserRequest;
import my.blog.bootdev.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
//회원 가입 폼에서 회원 가입 요청을 받으면 서비스 메서드를 사용해 사용자를 저장한 뒤, 로그인 페이지로 이동하는 signup() 메서드
public class UserApiController {

    private final UserService userService;

    @PostMapping("/user")
    public String signup(AddUserRequest request) {
        userService.save(request);//회원 가입 메서드 호출
        return "redirect:/login";//회원 가입이 완료된 이후에 로그인 페이지로 이동
    }
    //회원 가입 처리가 된 다음 로그인 페이지로 이동하기 위해서 redirect: 접두사를 사용함.
    //이렇게 처리하면 회원 가입 처리가 끝나면 강제로 /login URL에 해당하는 화면으로 이동함.

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }
    // /logout GET 요청을 하면 로그아웃을 담당하는 핸들러인 SecurityContextLogoutHandler의 logout()메서드를 호출해서 로그아웃함.

}
