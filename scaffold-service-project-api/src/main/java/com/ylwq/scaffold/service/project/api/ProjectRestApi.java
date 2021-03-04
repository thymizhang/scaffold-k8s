package com.ylwq.scaffold.service.project.api;

import com.ylwq.scaffold.service.project.dto.ProjectBudgetDto;
import com.ylwq.scaffold.service.project.dto.ProjectInfoDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目服务Api
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@RequestMapping("/project")
public interface ProjectRestApi {

    /**
     * 获取项目信息
     *
     * @param projectId 项目id
     * @return {@link ProjectInfoDto ProjectInfoDto}
     */
    @PostMapping("/getProjectInfo")
    ProjectInfoDto getProjectInfo(@RequestParam("projectId") String projectId);

    /**
     * 创建项目信息<br/>
     *
     * @param projectInfoDto 项目信息
     * @return true:创建成功  false：创建失败
     */
    @PostMapping("/addProjectInfo")
    boolean addProjectInfo(@RequestBody ProjectInfoDto projectInfoDto);

    /**
     * 编辑项目信息<br/>
     * 技术要点：项目信息修改后通过消息异步更新缓存中的公司项目列表
     *
     * @param projectInfoDto 项目信息
     * @return {@link ProjectInfoDto ProjectInfoDto}
     */
    @PostMapping("/editProjectInfo")
    ProjectInfoDto editProjectInfo(@RequestBody ProjectInfoDto projectInfoDto);

    /**
     * 获取公司所有项目信息
     *
     * @param companyId 公司id
     * @return 项目信息集合 {@link ProjectInfoDto ProjectInfoDto}
     */
    @PostMapping("/getCompanyProject")
    List<ProjectInfoDto> getCompanyProject(@RequestParam("companyId") String companyId);

    /**
     * 获取项目预算清单
     *
     * @param projectId 项目id
     * @return 预算清单集合 {@link ProjectBudgetDto ProjectBudgetDto}
     */
    @PostMapping("/getProjectBudgets")
    List<ProjectBudgetDto> getProjectBudgets(@RequestParam("projectId") String projectId);


    /**
     * RabbitMQ消息测试
     *
     * @param type direct | topic | fanout
     */
    @PostMapping("/messageSender")
    void messageSender(@RequestParam("type") String type);
}
