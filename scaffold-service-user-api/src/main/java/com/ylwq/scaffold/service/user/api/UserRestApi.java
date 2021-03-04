package com.ylwq.scaffold.service.user.api;

import com.ylwq.scaffold.service.user.dto.ComboDto;
import com.ylwq.scaffold.service.user.dto.UserInfoDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户服务Api<br/>
 * 技术要点：<br/>
 * 1. 统一使用@PostMapping，并指定url；<br/>
 * 2. 单个或多个参数使用@RequestParam标注，否则调用会报错；<br/>
 * 3. 对象参数使用@RequestBody标注，一个方法只能有一个@RequestBody；
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
    @PostMapping("/getNacosConfig")
    String getNacosConfig();

    /**
     * 获取微服务端口
     *
     * @return 微服务端口号
     */
    @PostMapping("/getServicePort")
    String getServicePort();

    /**
     * 获取服务套餐信息
     *
     * @return 服务套餐
     */
    @PostMapping("/getCombo")
    List<ComboDto> getCombo();

    /**
     * 获取用户服务调用地址
     *
     * @return 用户服务调用地址
     */
    @PostMapping("/getServiceUrl")
    String getServiceUrl();

    /**
     * 异常测试接口
     *
     * @param type 异常类型：null：Exception、"user"：UserException、"sys"：SystemException、"cli"：ClientException
     * @throws Exception 异常
     */
    @PostMapping("/testException")
    void testException(@RequestParam("type") String type) throws Exception;

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @PostMapping("/getUserInfo")
    UserInfoDto getUserInfo(@RequestParam("userId") String userId);

    /**
     * 获取公司成员
     *
     * @param companyId 公司id
     * @return 公司成员
     */
    @PostMapping("/getCompanyUser")
    List<UserInfoDto> getCompanyUser(@RequestParam("companyId") String companyId);

    /**
     * 编辑用户信息
     *
     * @param userInfoDto 用户信息
     * @return 用户信息
     */
    @PostMapping("/editUserInfo")
    UserInfoDto editUserInfo(@RequestBody UserInfoDto userInfoDto);

    /**
     * 编辑用户姓名
     *
     * @param userId   用户id
     * @param userName 用户姓名
     * @return 用户信息
     */
    @PostMapping("/editUserName")
    UserInfoDto editUserName(@RequestParam("userId") Integer userId, @RequestParam("userName") String userName);
}
