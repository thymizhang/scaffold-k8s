package com.ylwq.scaffold.service.company.controller;

import com.scaffold.service.company.api.CompanyRestApi;
import com.scaffold.service.company.dto.CompanyInfoDto;
import com.ylwq.scaffold.common.exception.UserErrorCode;
import com.ylwq.scaffold.common.exception.UserException;
import com.ylwq.scaffold.common.util.BeansUtil;
import com.ylwq.scaffold.service.company.entity.CompanyInfo;
import com.ylwq.scaffold.service.company.feign.UserRestFeign;
import com.ylwq.scaffold.service.company.service.CompanyInfoService;
import com.ylwq.scaffold.service.user.dto.UserInfoDto;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公司接口，面向微服务。
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Hidden
@RestController
public class CompanyRestController implements CompanyRestApi {

    final
    CompanyInfoService companyInfoService;

    final
    UserRestFeign userRestFeign;

    public CompanyRestController(CompanyInfoService companyInfoService, UserRestFeign userRestFeign) {
        this.companyInfoService = companyInfoService;
        this.userRestFeign = userRestFeign;
    }

    @Override
    public CompanyInfoDto getCompany(String companyId) {
        if(companyId == null){
            throw new UserException(UserErrorCode.A0400);
        }

        CompanyInfo companyInfo = companyInfoService.getById(companyId);
        if (companyInfo != null) {
            CompanyInfoDto companyInfoDto = new CompanyInfoDto();
            BeanUtils.copyProperties(companyInfo, companyInfoDto);
            /* 获取公司成员 */
            List<UserInfoDto> companyUser = userRestFeign.getCompanyUser(companyId);
            companyInfoDto.setUsers(companyUser);
            return companyInfoDto;
        } else {
            return null;
        }
    }

    @Override
    public String createCompany(CompanyInfoDto company) {
        if (company.getName() != null || company.getCreatorId() != null) {
            CompanyInfo companyInfo = new CompanyInfo();
            BeansUtil.copyProperties(company,companyInfo);

        }

        return null;
    }
}
