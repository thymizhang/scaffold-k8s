package com.ylwq.scaffold.service.company.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import com.nimbusds.jose.JWSObject;
import com.scaffold.service.company.api.CompanyRestApi;
import com.scaffold.service.company.dto.CompanyInfoDto;
import com.ylwq.scaffold.common.util.ResponseDataUtil;
import com.ylwq.scaffold.common.vo.ResponseData;
import com.ylwq.scaffold.service.user.dto.UserInfoDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;
import java.security.Principal;
import java.text.ParseException;

/**
 * 公司服务接口，使用统一对象返回给前端<br/>
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
@RequestMapping("/api/company")
@Api(tags = "公司服务接口")
public class CompanyController {

    final
    CompanyRestApi companyRestApi;

    public CompanyController(CompanyRestApi companyRestApi) {
        this.companyRestApi = companyRestApi;
    }

    /**
     * 获取公司信息及公司成员
     *
     * @param companyId 公司id
     * @return 公司信息
     */
    @ApiOperation("获取公司信息及公司成员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司id", dataTypeClass = String.class)
    })
    @SuppressWarnings("unchecked")
    @GetMapping("/{companyId}")
    @PreAuthorize("hasAnyAuthority('test','user','admin')")
    public ResponseData<CompanyInfoDto> getCompany(@PathVariable(value = "companyId") String companyId) {
        CompanyInfoDto company = companyRestApi.getCompany(companyId);
        return ResponseDataUtil.buildSuccess(company);
    }

    @GetMapping("/principal")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseData getPrincipal(Principal principal){
        return ResponseDataUtil.buildSuccess(principal);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseData getUser(Authentication authentication){
        return ResponseDataUtil.buildSuccess(authentication.getPrincipal());
    }

    @GetMapping("/jwt")
    @Secured("ROLE_ADMIN")
    public ResponseData getJwt(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");
        String realToken = token.replace("Bearer ", "");
        String userStr = "";
        UserInfoDto userInfoDto = new UserInfoDto();
        try {
            JWSObject jwsObject = JWSObject.parse(realToken);
            userStr = jwsObject.getPayload().toString();

            JSONObject jsonObject = new JSONObject(userStr);
            userInfoDto.setId(Convert.toLong(jsonObject.getStr("user_id")));
            userInfoDto.setUserName(jsonObject.getStr("user_name"));
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
        return ResponseDataUtil.buildSuccess(userInfoDto);
    }

    @ApiOperation("微服务接口调用测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataTypeClass = String.class)
    })
    @SuppressWarnings("unchecked")
    @PutMapping("/user/edit")
    public ResponseData<UserInfoDto> remoteTest(@RequestParam Integer userId,@RequestParam @Size(min = 3) String userName) {
        UserInfoDto userInfoDto = companyRestApi.remoteTest(userId, userName);
        return ResponseDataUtil.buildSuccess(userInfoDto);
    }
}
