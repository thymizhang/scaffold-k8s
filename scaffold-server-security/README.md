## 项目说明
认证授权


## 技术要点
一、认证授权配置；
二、认证授权token
三、认证授权与网关整合；


### 一、认证授权配置
1. 配置密码加密规则，SecurityConfig.getPasswordEncoder
2. 实现自定义密码校验逻辑，UserDetailsServiceImpl.loadUserByUsername
3. 自定义登录页面和登录错误页面，及页面访问授权，SecurityConfig.configure
4. 自定义登录成功跳转页面，LoginAuthenticationSuccessHandler
5. 自定义登录失败跳转页面，LoginAuthenticationFailureHandler
5. 自定义无授权异常，LoginAccessDeniedHandler


问题：
* 在security工程中通过feign调用user服务失败
> 错误信息：RibbonLoadBalancerClient does not define or inherit an implementation of the resolved method abstract choose
> 解决：经测试是springboot与springcloud版本不兼容导致，这里我们改为最后的稳定兼容版本，问题解决。