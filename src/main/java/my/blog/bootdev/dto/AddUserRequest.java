package my.blog.bootdev.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//사용자 정보를 담고 있는 객체
@Setter
@Getter
public class AddUserRequest {
    private String email;
    private String password;
}
