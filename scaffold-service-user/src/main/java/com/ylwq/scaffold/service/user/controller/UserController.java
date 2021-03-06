package com.ylwq.scaffold.service.user.controller;

import com.ylwq.scaffold.common.util.ResponseDataUtil;
import com.ylwq.scaffold.common.vo.ResponseData;
import com.ylwq.scaffold.service.user.api.UserRestApi;
import com.ylwq.scaffold.service.user.dto.UserInfoDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户服务接口，使用统一对象返回给前端<br/>
 * <p>
 * 技术要点：<br/>
 * 1. 调用链说明：controller->rest->service->dao；<br/>
 * 2. controller层：为前端提供接口，返回统一对象{@link ResponseData ResponseData}；<br/>
 * 3. rest层：为controller层和其他微服务提供接口，参数校验，业务逻辑实现，返回DTO对象；<br/>
 * 4. service层：为rest层提供实体类操作接口，封装dao，丰富操作，返回Entity对象；<br/>
 * 5. dao（mapper）层：为service层提供实体类基本CRUD操作，自定义查询，返回Entity对象；
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@RestController
@RequestMapping("/api/user")
@RefreshScope
@Api(tags = "用户服务接口")
public class UserController {

    final
    UserRestApi userRestApi;

    /**
     * 构造函数<br/>
     * 官方文档表示{@code @Autowired}可以省略
     *
     * @param userRestApi 用户服务Rest
     */
    @Autowired
    public UserController(UserRestApi userRestApi) {
        this.userRestApi = userRestApi;
    }

    /**
     * 异常测试接口，所有异常通过全局异常处理类{@link com.ylwq.scaffold.service.user.handler.GlobalControllerExceptionHandler GlobalControllerExceptionHandler}进行拦截
     *
     * @param type 异常类型：null|user|sys|cli
     * @return ResponseData
     * @throws Exception 异常
     */
    @ApiOperation("异常测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "异常类型：null|user|sys|cli", dataTypeClass = String.class)
    })
    @GetMapping("/err/{type}")
    public ResponseData testError(@PathVariable String type) throws Exception {
        userRestApi.testException(type);
        return ResponseDataUtil.buildSuccess("请尝试参数：null | user | sys | client");
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return data：{@link UserInfoDto UserInfoDto}
     */
    @ApiOperation("获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataTypeClass = String.class)
    })
    @GetMapping("/{userId}")
    public ResponseData getUserInfo(@PathVariable String userId) {
        UserInfoDto userInfo = userRestApi.getUserInfo(userId);
        if (userInfo != null) {
            return ResponseDataUtil.buildSuccess(userInfo);
        } else {
            return ResponseDataUtil.buildError();
        }
    }

    /**
     * 获取公司成员
     *
     * @param companyId 公司id
     * @return data：{@link UserInfoDto UserInfoDto}
     */
    @ApiOperation("获取公司成员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司id", dataTypeClass = String.class)
    })
    @GetMapping("/companyUser/{companyId}")
    public ResponseData getCompanyUser(@PathVariable String companyId) {
        if (companyId == null) {
            return ResponseDataUtil.buildError();
        }
        List<UserInfoDto> companyUser = userRestApi.getCompanyUser(companyId);
        return ResponseDataUtil.buildSuccess(companyUser);
    }

    @ApiOperation("编辑用户信息")
    @SuppressWarnings("unchecked")
    @PutMapping("/edit")
    public ResponseData<UserInfoDto> editUserInfo(@RequestBody @Validated UserInfoDto userInfoDto) {
        if (userInfoDto == null) {
            return ResponseDataUtil.buildError();
        }
        UserInfoDto userinfo = userRestApi.editUserInfo(userInfoDto);
        return ResponseDataUtil.buildSuccess(userinfo);
    }

}
