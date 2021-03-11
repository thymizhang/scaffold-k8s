## 项目说明
认证授权


1. 配置类，注入密码加密规则，SecurityConfig
2. 服务类，实现自定义密码校验逻辑，UserDetailsServiceImpl
3. 配置类，


问题：
* 在security工程中通过feign调用user服务失败
> 错误信息：RibbonLoadBalancerClient does not define or inherit an implementation of the resolved method abstract choose
