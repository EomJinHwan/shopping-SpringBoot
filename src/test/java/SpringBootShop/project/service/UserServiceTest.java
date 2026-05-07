package SpringBootShop.project.service;

import SpringBootShop.project.controller.UserForm;
import SpringBootShop.project.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService service;

    @Test
    void singUp() {
        // given & when
        User user = service.singUp("qwer", "12345");

        // then
        assertThat(service.findByUserId("qwer")).isEqualTo(user);
    }

    @Test
    void checkId() {
        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class,
                () -> service.checkId("asd"));
        assertThat(e1.getMessage()).isEqualTo("아이디는 4~7글자로 해주세요");

        service.singUp("asdf", "12345");
        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class,
                () -> service.checkId("asdf"));
        assertThat(e2.getMessage()).isEqualTo("중복된 아이디 입니다");
    }

    @Test
    void checkPw() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> service.checkPw("1234"));
        assertThat(e.getMessage()).isEqualTo("비밀번호를 5자리 이상으로 해주세요");
    }

    @Test
    void login() {
        User singUp = service.singUp("qwer", "12345");
        User login = service.login("qwer", "12345");
        assertThat(singUp).isEqualTo(login);
    }

    @Test
    void login_fail() {
        service.singUp("qwer", "12345");

        IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class,
                () -> service.login("qwert", "12345"));
        assertThat(e1.getMessage()).isEqualTo("아이디 또는 비밀번호가 일치하지 않습니다");

        IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class,
                () -> service.login("qwer", "12345678"));
        assertThat(e2.getMessage()).isEqualTo("아이디 또는 비밀번호가 일치하지 않습니다");
    }

    @Test
    void findByUserId() {
        User user = service.singUp("qwer", "12345");
        User result = service.findByUserId(user.getUserId());
        assertThat(result).isEqualTo(user);
    }

    @Test
    void updatePw() {
        User user = service.singUp("qwer", "12345");

        UserForm form = new UserForm();
        form.setUserPw("54321");

        service.updatePw(user.getUserId(), form);

        User result = service.findByUserId(user.getUserId());
        assertThat(result.getUserPw()).isEqualTo("54321");
    }
}