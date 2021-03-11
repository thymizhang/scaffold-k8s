import com.ylwq.scaffold.server.security.SecurityApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码测试
 *
 * @Author thymi
 * @Date 2021/3/9
 */
@SpringBootTest(classes = {SecurityApplication.class})
@Slf4j
public class PasswordTest {

    @Test
    public void getPassword() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        /* 常用测试密码的随机盐加密结果 */
        log.info("123456 : " + bCryptPasswordEncoder.encode("123456"));
        log.info("654321 : " + bCryptPasswordEncoder.encode("654321"));
        log.info("111111 : " + bCryptPasswordEncoder.encode("111111"));

        log.info("结果：" + bCryptPasswordEncoder.matches("123456", "$2a$10$fZ7MMdOu7kBktpYCkXnQxOt4LaTBJJGBsQZk2m1c6Y/XJMMR2MGxq"));

    }
}
