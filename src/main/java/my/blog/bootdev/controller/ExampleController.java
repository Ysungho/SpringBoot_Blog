package my.blog.bootdev.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller//컨트롤러라는 것을 명시적으로 표시
//스프링 부트는 *@Controller 애너테이션을 보고 반환하는 값의 이름을 가진 뷰의 파일을 찾으라고 이해한다.*
public class ExampleController {

    @GetMapping("/thymeleaf/example")
    public String thymeleafExample(Model model) {//뷰로 데이터를 넘겨주는 모델 객체
        //Model은 html쪽으로 값을 넘겨주는 객체
        //모델 객체는 따로 생성할 필요 없이 코드처럼 인자로 선언하기만 하면 스프링이 알아서 만들어 주므로 편리하게 사용할 수 있음.
        //컨트롤러는 모델을 통해 데이터를 설정하고, 모델은 뷰로 이 데이터를 전달해 키에 맞는 데이터를 뷰에서 조회할 수 있게 만듬.
        //즉 모델은 컨트롤러와 뷰의 중간다리 역할을 하는 것임.
        Person examplePerson = new Person();
        examplePerson.setId(1L);
        examplePerson.setName("홍길동");
        examplePerson.setAge(11);
        examplePerson.setHobbies(List.of("운동", "독서"));

        model.addAttribute("person", examplePerson);//Person 객체 저장
        model.addAttribute("today", LocalDate.now());
        //addAttribute() 메서드로 모델에 값을 저장함.

        return "example";//example.html라는 뷰 조회
    }

    @Setter
    @Getter
    class Person {
        private Long id;
        private String name;
        private int age;
        private List<String> hobbies;
    }
}

