import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ylwq.scaffold.service.project.ProjectApplication;
import com.ylwq.scaffold.service.project.entity.ProjectBudget;
import com.ylwq.scaffold.service.project.entity.ProjectInfo;
import com.ylwq.scaffold.service.project.service.ProjectBudgetService;
import com.ylwq.scaffold.service.project.service.ProjectInfoService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目预算清单服务测试
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ProjectApplication.class})
public class ProjectBudgetServiceTest {

    @Autowired
    ProjectInfoService projectInfoService;

    @Autowired
    ProjectBudgetService projectBudgetService;

    @Test
    public void save() {
        /* 获取公司1的项目 */
        LambdaQueryWrapper<ProjectInfo> lambdaQueryWrapper1 = Wrappers.lambdaQuery();
        lambdaQueryWrapper1.eq(ProjectInfo::getCompanyId, 1L);
        List<ProjectInfo> list1 = projectInfoService.list(lambdaQueryWrapper1);
        List<Long> projectIds1 = list1.stream().map(ProjectInfo::getId).collect(Collectors.toList());
        System.out.println("公司1的所有项目id：" + projectIds1);
        /* 获取公司2的项目 */
        LambdaQueryWrapper<ProjectInfo> lambdaQueryWrapper2 = Wrappers.lambdaQuery();
        lambdaQueryWrapper2.eq(ProjectInfo::getCompanyId, 2L);
        List<ProjectInfo> list2 = projectInfoService.list(lambdaQueryWrapper2);
        List<Long> projectIds2 = list2.stream().map(ProjectInfo::getId).collect(Collectors.toList());
        System.out.println("公司2的所有项目id：" + projectIds2);

        List<ProjectBudget> budgets = Lists.newArrayList();
        /* 采用雪花算法让清单名唯一 */
        Snowflake snowflake = IdUtil.getSnowflake(7, 7);
        for (int i = 0; i < 100; i++) {
            System.out.println(i + " : " + IdUtil.randomUUID());
            long id = snowflake.nextId();
            ProjectBudget projectBudget = new ProjectBudget();
            projectBudget.setCompanyId(id % 2 + 1);
            if (projectBudget.getCompanyId() == 1L) {
                if (projectIds1.size() != 0) {
                    int index = (int) (id % projectIds1.size());
                    projectBudget.setProjectId(projectIds1.get(index));
                }
            } else if (projectBudget.getCompanyId() == 2L) {
                if (projectIds2.size() != 0) {
                    int index = (int) (id % projectIds2.size());
                    projectBudget.setProjectId(projectIds2.get(index));
                }
            }

            projectBudget.setName("大理石" + id);
            /* 采用UUID让清单描述唯一 */
            projectBudget.setSpec(IdUtil.randomUUID());
            projectBudget.setUnit("块");
            budgets.add(projectBudget);
        }
        boolean save = projectBudgetService.saveBatch(budgets);
        if (save) {
            System.out.println("清单批量创建成功");
        }
    }

    /**
     * 删除所有数据
     */
    @Test
    public void delete() {
        LambdaQueryWrapper<ProjectBudget> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(ProjectBudget::getCompanyId, 1L);
        projectBudgetService.remove(lambdaQueryWrapper);
    }
}
