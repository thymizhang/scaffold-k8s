package com.ylwq.scaffold.service.project.controller;

import com.google.common.collect.Lists;
import com.ylwq.scaffold.common.util.ResponseDataUtil;
import com.ylwq.scaffold.common.vo.ResponseData;
import com.ylwq.scaffold.service.project.api.ProjectRestApi;
import com.ylwq.scaffold.service.project.dto.ProjectInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目服务接口
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@RestController
@RequestMapping("/api/project")
@Tag(name = "项目服务接口")
public class ProjectController {

    final
    ProjectRestApi projectRestApi;

    public ProjectController(ProjectRestApi projectRestApi) {
        this.projectRestApi = projectRestApi;
    }

    @Operation(summary = "获取公司项目",
            parameters = {
                    @Parameter(name = "companyId", description = "公司id")
            },
            security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
    @GetMapping("/company/{companyId}")
    public ResponseData getCompanyProjects(@PathVariable String companyId) {
        List<ProjectInfoDto> companyProject = projectRestApi.getCompanyProject(companyId);
        return ResponseDataUtil.buildSuccess(companyProject);
    }

    @Operation(summary = "添加项目",
            security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
    @PostMapping("/projectInfo/add")
    public ResponseData addProject(@RequestBody ProjectInfoDto projectInfoDto) {
        return null;
    }

    @Operation(summary = "编辑项目信息",
            security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
    @PutMapping("/projectInfo/edit")
    public ResponseData editProjectInfo(@RequestBody ProjectInfoDto projectInfoDto) {
        return ResponseDataUtil.buildSuccess(projectRestApi.editProjectInfo(projectInfoDto));
    }

    @Operation(summary = "RabbitMQ消息测试",
            parameters = {
                    @Parameter(name = "type", description = "direct | topic | fanout")
            },
            security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
    @GetMapping("/send/{type}")
    public ResponseData messageSender(@PathVariable String type) {
        List<String> types = Lists.newArrayList("direct", "topic", "fanout");
        if (types.contains(type)) {
            projectRestApi.messageSender(type);
            return ResponseDataUtil.buildSuccess();
        }
        return ResponseDataUtil.buildFaild("请输入：direct | topic | fanout");
    }

}
