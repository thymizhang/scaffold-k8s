import com.ylwq.scaffold.service.user.UserApplication;
import com.ylwq.scaffold.service.user.entity.UserLoginLog;
import com.ylwq.scaffold.service.user.service.UserLoginLogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @Author thymi
 * @Date 2021/1/7
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserApplication.class)
public class UserLoginLogServiceTest {

    @Autowired
    UserLoginLogService userLoginLogService;

    @Test
    public void save() {
        for (int i = 1; i < 20001; i++) {
            long modId = i % 6 + 1;
            long ip = i % 254 + 1;
            UserLoginLog userLoginLog = new UserLoginLog();
            userLoginLog.setUserId(modId);
            userLoginLog.setIp("192.168.213." + ip);
            userLoginLogService.save(userLoginLog);
        }
    }
}
