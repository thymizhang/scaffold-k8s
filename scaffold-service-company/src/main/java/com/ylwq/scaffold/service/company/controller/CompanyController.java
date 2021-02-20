package com.ylwq.scaffold.service.company.controller;

import com.scaffold.service.company.api.CompanyRestApi;
import com.scaffold.service.company.dto.CompanyInfoDto;
import com.ylwq.scaffold.common.util.ResponseDataUtil;
import com.ylwq.scaffold.common.vo.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Tag(name = "公司服务接口")
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
    @Operation(summary = "获取公司信息及公司成员",
            parameters = {
                    @Parameter(name = "companyId", description = "公司id")
            },
            security = @SecurityRequirement(name = "需要认证"))
    @GetMapping("/{companyId}")
    public ResponseData<CompanyInfoDto> getCompany(@PathVariable(value = "companyId") String companyId) {
        CompanyInfoDto company = companyRestApi.getCompany(companyId);
        return ResponseDataUtil.buildSuccess(company);
    }
}
