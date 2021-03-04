package com.ylwq.scaffold.service.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ylwq.scaffold.common.util.BeansUtil;
import com.ylwq.scaffold.service.project.amqp.ProjectAmqpSender;
import com.ylwq.scaffold.service.project.api.ProjectRestApi;
import com.ylwq.scaffold.service.project.cache.ProjectCache;
import com.ylwq.scaffold.service.project.dto.ProjectBudgetDto;
import com.ylwq.scaffold.service.project.dto.ProjectInfoDto;
import com.ylwq.scaffold.service.project.entity.ProjectBudget;
import com.ylwq.scaffold.service.project.entity.ProjectInfo;
import com.ylwq.scaffold.service.project.service.ProjectBudgetService;
import com.ylwq.scaffold.service.project.service.ProjectInfoService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 项目服务接口，面向项目及其他微服务
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@RestController
@Hidden
@Slf4j
public class ProjectRestController implements ProjectRestApi {

    final
    ProjectInfoService projectInfoService;

    final
    ProjectBudgetService projectBudgetService;

    final
    ProjectAmqpSender projectAmqpSender;

    final
    ProjectCache projectCache;

    public ProjectRestController(ProjectInfoService projectInfoService, ProjectBudgetService projectBudgetService, ProjectCache projectCache, ProjectAmqpSender projectAmqpSender) {
        this.projectInfoService = projectInfoService;
        this.projectBudgetService = projectBudgetService;
        this.projectCache = projectCache;
        this.projectAmqpSender = projectAmqpSender;
    }

    @Override
    public ProjectInfoDto getProjectInfo(String projectId) {
        ProjectInfo projectInfo = projectInfoService.getById(projectId);
        ProjectInfoDto projectInfoDto = new ProjectInfoDto();
        BeansUtil.copyProperties(projectInfo, projectInfoDto);
        return projectInfoDto;
    }

    @Override
    public boolean addProjectInfo(ProjectInfoDto projectInfoDto) {
        ProjectInfo projectInfo = new ProjectInfo();
        BeansUtil.copyProperties(projectInfoDto, projectInfo);
        return projectInfoService.save(projectInfo);
    }

    @Override
    public ProjectInfoDto editProjectInfo(ProjectInfoDto projectInfoDto) {
        ProjectInfo projectInfo = new ProjectInfo();
        BeansUtil.copyProperties(projectInfoDto, projectInfo);
        boolean isOk = projectInfoService.updateById(projectInfo);
        if (isOk) {
            projectInfo = projectInfoService.getById(projectInfoDto.getId());
            BeansUtil.copyProperties(projectInfo, projectInfoDto);
            /* 项目信息更新成功后，发消息通知更新公司项目列表缓存 */
            projectAmqpSender.projectUpdate(projectInfo.getCompanyId());
            return projectInfoDto;
        }
        return null;
    }

    @Override
    public List<ProjectInfoDto> getCompanyProject(String companyId) {
        List<ProjectInfoDto> companyProject = this.projectCache.pullCompanyProjects(companyId);
        if (companyProject.size() == 0) {
            /* 如果缓存没有数据，从数据库获取，同时将数据缓存 */
            LambdaQueryWrapper<ProjectInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
            lambdaQueryWrapper.eq(ProjectInfo::getCompanyId, Long.valueOf(companyId));
            List<ProjectInfo> projectInfos = projectInfoService.list(lambdaQueryWrapper);
            companyProject = BeansUtil.copyList(projectInfos, ProjectInfoDto.class);
            /* 进行缓存 */
            this.projectCache.pushCompanyProjects(companyId, projectInfos);
        }
        return companyProject;
    }

    @Override
    public List<ProjectBudgetDto> getProjectBudgets(String projectId) {
        LambdaQueryWrapper<ProjectBudget> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(ProjectBudget::getProjectId, projectId);
        List<ProjectBudget> projectBudgets = projectBudgetService.list(lambdaQueryWrapper);
        return BeansUtil.copyList(projectBudgets, ProjectBudgetDto.class);
    }

    @Override
    public void messageSender(String type) {
        String direct = "direct";
        String topic = "topic";
        String fanout = "fanout";
        if (direct.equals(type)) {
            projectAmqpSender.directExchangeSender("project.add");
        }
        if (topic.equals(type)) {
            projectAmqpSender.topicExchangeSender("company.name");
        }
        if (fanout.equals(type)) {
            projectAmqpSender.fanoutExchangeSender();
        }
    }
}
