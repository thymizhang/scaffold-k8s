import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ylwq.scaffold.common.util.JsonUtil;
import com.ylwq.scaffold.service.project.ProjectApplication;
import com.ylwq.scaffold.service.project.api.ProjectRestApi;
import com.ylwq.scaffold.service.project.dao.ProjectInfoMapper;
import com.ylwq.scaffold.service.project.dto.ProjectInfoDto;
import com.ylwq.scaffold.service.project.entity.ProjectInfo;
import com.ylwq.scaffold.service.project.service.ProjectInfoService;
import org.apache.shardingsphere.api.hint.HintManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目信息服务测试
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ProjectApplication.class})
public class ProjectInfoServiceTest {

    @Autowired
    ProjectRestApi  projectRestApi;

    @Autowired
    ProjectInfoService projectInfoService;

    @Autowired
    ProjectInfoMapper projectInfoMapper;

    Long creatorId = 3L;

    @Test
    public void save() throws InterruptedException {
        ProjectInfo projectInfo1 = new ProjectInfo();
        projectInfo1.setCompanyId(1L);
        projectInfo1.setName("天宇广场");
        projectInfo1.setCreatorId(this.creatorId);
        ProjectInfo projectInfo2 = new ProjectInfo();
        projectInfo2.setCompanyId(2L);
        projectInfo2.setName("海晴大厦");
        projectInfo2.setCreatorId(this.creatorId);
        ProjectInfo projectInfo3 = new ProjectInfo();
        projectInfo3.setCompanyId(1L);
        projectInfo3.setName("江华大楼");
        projectInfo3.setCreatorId(this.creatorId);
        ProjectInfo projectInfo4 = new ProjectInfo();
        projectInfo4.setCompanyId(1L);
        projectInfo4.setName("湖锦酒店");
        projectInfo4.setCreatorId(this.creatorId);
        if (projectInfoService.save(projectInfo1)) {
            System.out.println("项目创建成功，项目：" + projectInfo1.getId());
        }
        if (projectInfoService.save(projectInfo2)) {
            System.out.println("项目创建成功，项目：" + projectInfo2.getId());
        }
        if (projectInfoService.save(projectInfo3)) {
            System.out.println("项目创建成功，项目：" + projectInfo3.getId());
        }
        if (projectInfoService.save(projectInfo4)) {
            System.out.println("项目创建成功，项目：" + projectInfo4.getId());
        }
    }

    @Test
    public void update() throws InterruptedException {
        ProjectInfo projectInfo = this.list("天宇广场");
        ProjectInfo project = new ProjectInfo();
        project.setUserCount(33);

        LambdaQueryWrapper<ProjectInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(ProjectInfo::getId, projectInfo.getId());
        lambdaQueryWrapper.eq(ProjectInfo::getCompanyId, projectInfo.getCompanyId());

        projectInfoService.update(project,lambdaQueryWrapper);
    }

    @Test
    public void get() throws InterruptedException {
        ProjectInfo projectInfo = this.list("天宇广场");

        LambdaQueryWrapper<ProjectInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(ProjectInfo::getId, projectInfo.getId());
        lambdaQueryWrapper.eq(ProjectInfo::getCompanyId, projectInfo.getCompanyId());

        ProjectInfo project = projectInfoService.getOne(lambdaQueryWrapper);
        System.out.println(project);
    }

    @Test
    public ProjectInfo list(String projectName) throws InterruptedException {
        Thread.sleep(2000);
        LambdaQueryWrapper<ProjectInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(ProjectInfo::getName, projectName);
        // 强制使用主库
        //HintManager.getInstance().setMasterRouteOnly();
        ProjectInfo projectInfo = projectInfoService.getOne(lambdaQueryWrapper);
        System.out.println(JsonUtil.objectToJson(projectInfo));
        return projectInfo;
    }

    @Test
    public void getIds(){
        List<ProjectInfo> list = projectInfoService.list();
        List<Long> collect = list.stream().map(ProjectInfo::getId).collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void getCompanyProject(){
        this.projectRestApi.getCompanyProject("1");
    }
}
