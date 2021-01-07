package com.ylwq.scaffold.service.user.api;

import com.ylwq.scaffold.service.user.dto.ComboDto;
import com.ylwq.scaffold.service.user.dto.UserInfoDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 用户服务Api<br/>
 * 技术要点：<br/>
 * 1. 服务间调用接口，如果使用`@PathVariable(value = "")`，其中的`value`不能省略；
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@RequestMapping("/user")
public interface UserRestApi {

    /**
     * 获取Nacos中指定的配置文件（data_id）内容
     *
     * @return 配置文件内容
     */
    @GetMapping("/nacos")
    String getNacosConfig();

    /**
     * 获取微服务端口
     *
     * @return 微服务端口号
     */
    @GetMapping("/port")
    String getServicePort();

    /**
     * 获取服务套餐信息
     *
     * @return 服务套餐
     */
    @GetMapping("/combo")
    List<ComboDto> getCombo();

    /**
     * 获取用户服务调用地址
     *
     * @return 用户服务调用地址
     */
    @GetMapping("/url")
    String getServiceUrl();

    /**
     * 异常测试接口
     *
     * @param type 异常类型：null：Exception、"user"：UserException、"sys"：SystemException、"cli"：ClientException
     * @throws Exception
     */
    @GetMapping("/ept/{type}")
    void testException(@PathVariable(value = "type") String type) throws Exception;

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @GetMapping("/userInfo/{userId}")
    UserInfoDto getUserInfo(@PathVariable(value = "userId") String userId);

    /**
     * 获取公司成员
     *
     * @param companyId 公司id
     * @return 公司成员
     */
    @GetMapping("/companyUser/{companyId}")
    List<UserInfoDto> getCompanyUser(@PathVariable(value = "companyId") String companyId);

    /**
     * 编辑用户信息
     *
     * @param userInfoDto 用户信息
     * @return UserInfoDto
     */
    @PostMapping("/edit")
    UserInfoDto editUserInfo(UserInfoDto userInfoDto);
}
