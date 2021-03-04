package com.scaffold.service.company.api;

import com.scaffold.service.company.dto.CompanyInfoDto;
import com.ylwq.scaffold.service.user.dto.UserInfoDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
     *
     * @param companyId 公司id
     * @return 公司信息
     */
    @PostMapping("/getCompany")
    CompanyInfoDto getCompany(@RequestParam("companyId") String companyId);

    /**
     * 创建公司
     *
     * @param companyInfoDto 公司信息
     * @return 公司id
     */
    @PostMapping("/createCompany")
    boolean createCompany(@RequestBody CompanyInfoDto companyInfoDto);

    /**
     * 远程调用测试
     * @param userId userId
     * @param userName userName
     * @return UserInfoDto
     */
    @RequestMapping
    UserInfoDto remoteTest(Integer userId, String userName);
}
