package com.ylwq.scaffold.service.user.controller;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ylwq.scaffold.common.exception.*;
import com.ylwq.scaffold.common.util.BeansUtil;
import com.ylwq.scaffold.common.util.JsonUtil;
import com.ylwq.scaffold.common.vo.ResponseData;
import com.ylwq.scaffold.service.user.entity.UserInfo;
import com.ylwq.scaffold.service.user.service.UserInfoService;
import com.ylwq.scaffold.service.user.api.UserRestApi;
import com.ylwq.scaffold.service.user.dto.ComboDto;
import com.ylwq.scaffold.service.user.dto.UserInfoDto;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Properties;

/**
 * 用户服务接口，面向其他微服务<br/>
 * <p>
 * 一、技术要点：<br/>
 * 1. 在类开头使用`@RefreshScope` 可自动刷新配置信息；<br/>
 * 2. 在属性使用`@Value` 可读取系统配置信息；<br/>
 * 3. 使用统一返回对象{@link ResponseData ResponseData}；<br/>
 * 4. 返回数据Entity使用{@link BeansUtil BeansUtil}转换为DTO;<br/>
 * <p>
 * 二、接口要点：<br/>
 * 1. 服务实现和其他微服务调用（Feign）均使用同一个接口，可以简化开发；<br/>
 * 2. RequestMapping在接口中声明，在实现和调用中无需再声明；<br/>
 * <p>
 * 三、对象中的null值解决方案：<br/>
 * 1. 在类开头加上@JsonInclude(JsonInclude.Include.NON_NULL)，将去掉null值属性；<br/>
 * 2. 将null转换为空串，详见{@link com.ylwq.scaffold.common.util.JsonUtil JsonUtil}；<br/>
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@RestController
@RefreshScope
@Hidden
public class UserRestController implements UserRestApi {

    /**
     * 微服务配置端口
     * <p>
     * `@Value` 可读取系统配置信息，配置来自配置文件*.yml、*.properties
     */
    @Value("${server.port}")
    private String port;

    /**
     * 服务套餐信息
     * <p>
     * 技术要点：数据来自nacos配置中心的自定义配置文件combo.properties
     */
    @Value("${combo}")
    private String combo;

    /**
     * 服务调用地址
     */
    @Value("${rpc.service.user.url}")
    private String serviceUrl;

    final
    UserInfoService userInfoService;

    public UserRestController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Override
    public String getNacosConfig() {
        String content = "";
        try {
            String serverAddr = "nacos.gceasy.cc:8848";
            String dataId = "combo.properties";
            String group = "SCAFFOLD";
            String nameSpace = "SCAFFOLD-DEV";
            Properties properties = new Properties();
            properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
            properties.put(PropertyKeyConst.NAMESPACE, nameSpace);
            ConfigService configService = NacosFactory.createConfigService(properties);
            content = configService.getConfig(dataId, group, 5000);
            System.out.println("配置文件内容：" + content);

        } catch (NacosException e) {
            e.printStackTrace();
        }
        return content;
    }

    @Override
    public String getServicePort() {
        return this.port;
    }

    @Override
    public List<ComboDto> getCombo() {
        return JsonUtil.jsonToList(this.combo, ComboDto.class);
    }

    @Override
    public String getServiceUrl() {
        return this.serviceUrl;
    }

    @Override
    public void testException(String type) throws Exception {
        String user = "user";
        String sys = "sys";
        String client = "client";
        String strNull = "null";

        if (strNull.equals(type)) {
            throw new Exception("非监控系统异常");
        }

        if (user.equals(type)) {
            throw new UserException(UserErrorCode.A0001);
        }

        if (sys.equals(type)) {
            throw new SystemException(SystemErrorCode.B0001);
        }

        if (client.equals(type)) {
            throw new ClientException(ClientErrorCode.C0001);
        }
    }

    @Override
    public UserInfoDto getUserInfo(String userId) {
        UserInfo userInfo = userInfoService.getById(userId);
        if (userInfo != null) {
            UserInfoDto userInfoDto = new UserInfoDto();
            BeansUtil.copyProperties(userInfo, userInfoDto);
            return userInfoDto;
        }
        return null;
    }

    @Override
    public List<UserInfoDto> getCompanyUser(String companyId) {
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(UserInfo::getCompanyId, companyId);
        List<UserInfo> users = userInfoService.list(lambdaQueryWrapper);
        return BeansUtil.copyList(users, UserInfoDto.class);
    }

    @Override
    public UserInfoDto editUserInfo(UserInfoDto userInfoDto) {
        if (userInfoDto == null) {
            throw new UserException(UserErrorCode.A0410);
        }
        UserInfo userInfo = new UserInfo();
        BeansUtil.copyProperties(userInfoDto, userInfo);

        boolean success = userInfoService.updateById(userInfo);
        if (success) {
            return userInfoDto;
        } else {
            return null;
        }
    }

    @Override
    public UserInfoDto editUserName(Integer userId, String userName) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(Long.valueOf(userId));
        userInfo.setUserName(userName);
        boolean success = userInfoService.updateById(userInfo);
        if (success) {
            UserInfoDto userInfoDto = new UserInfoDto();
            userInfo = userInfoService.getById(userId);
            BeansUtil.copyProperties(userInfo, userInfoDto);
            return userInfoDto;
        } else {
            return null;
        }
    }
}
