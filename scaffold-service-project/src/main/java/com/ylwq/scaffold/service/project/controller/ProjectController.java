package com.ylwq.scaffold.service.project.controller;

import com.google.common.collect.Lists;
import com.ylwq.scaffold.common.util.ResponseDataUtil;
import com.ylwq.scaffold.common.vo.ResponseData;
import com.ylwq.scaffold.service.project.api.ProjectRestApi;
import com.ylwq.scaffold.service.project.dto.ProjectInfoDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
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
@Api(tags = "项目服务接口")
public class ProjectController {

    final
    ProjectRestApi projectRestApi;

    public ProjectController(ProjectRestApi projectRestApi) {
        this.projectRestApi = projectRestApi;
    }

    @ApiOperation("获取公司项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司id", dataTypeClass = String.class)
    })
    @GetMapping("/company/{companyId}")
    public ResponseData getCompanyProjects(@PathVariable String companyId) {
        List<ProjectInfoDto> companyProject = projectRestApi.getCompanyProject(companyId);
        return ResponseDataUtil.buildSuccess(companyProject);
    }

    @ApiOperation("添加项目")
    @PostMapping("/add")
    public ResponseData addProject(@RequestBody @Validated ProjectInfoDto projectInfoDto) {
        boolean isOk = projectRestApi.addProjectInfo(projectInfoDto);
        if (isOk) {
            return ResponseDataUtil.buildSuccess();
        } else {
            return ResponseDataUtil.buildFaild();
        }
    }

    @ApiOperation("编辑项目信息")
    @PutMapping("/edit")
    public ResponseData editProjectInfo(@RequestBody @Validated ProjectInfoDto projectInfoDto) {
        return ResponseDataUtil.buildSuccess(projectRestApi.editProjectInfo(projectInfoDto));
    }

    @ApiOperation("RabbitMQ消息测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "direct | topic | fanout", dataTypeClass = String.class)
    })
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
