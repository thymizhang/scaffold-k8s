import com.ylwq.scaffold.service.company.CompanyApplication;
import com.ylwq.scaffold.service.company.entity.CompanyInfo;
import com.ylwq.scaffold.service.company.feign.UserRestFeign;
import com.ylwq.scaffold.service.company.service.CompanyInfoService;
import com.ylwq.scaffold.service.user.dto.UserInfoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 公司信息服务测试
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {CompanyApplication.class})
public class CompanyInfoServiceTest {

    @Autowired
    CompanyInfoService companyInfoService;

    @Autowired
    UserRestFeign userRestFeign;

    /**
     * 用户id，测试用
     */
    Long userId = 3L;

    @Test
    public void save() {
        UserInfoDto userInfo = userRestFeign.getUserInfo(this.userId.toString());
        CompanyInfo companyInfo1 = new CompanyInfo();
        companyInfo1.setName("百工云建集团");
        companyInfo1.setUserCount("0");
        CompanyInfo companyInfo2 = new CompanyInfo();
        companyInfo2.setName("阡陌建工股份有限公司");
        companyInfo2.setUserCount("1");
        if (userInfo != null) {
            companyInfo1.setCreatorId(this.userId);
            companyInfoService.save(companyInfo1);
            companyInfo2.setCreatorId(this.userId);
            companyInfoService.save(companyInfo2);
        }
    }
}
