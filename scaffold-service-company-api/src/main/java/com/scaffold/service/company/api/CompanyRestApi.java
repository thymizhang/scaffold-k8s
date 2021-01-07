package com.scaffold.service.company.api;

import com.scaffold.service.company.dto.CompanyInfoDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 公司服务Api
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@RequestMapping("/company")
public interface CompanyRestApi {

    /**
     * 获取公司信息及公司成员
     * `@RequestMapping("/{companyId}")`
     *
     * @param companyId 公司id
     * @return 公司信息
     */
    @GetMapping("/{companyId}")
    CompanyInfoDto getCompany(@PathVariable(value = "companyId") String companyId);

    /**
     * 创建公司
     *
     * @param company 公司信息
     * @return 公司id
     */
    @PostMapping("/create")
    String createCompany(CompanyInfoDto company);

}
