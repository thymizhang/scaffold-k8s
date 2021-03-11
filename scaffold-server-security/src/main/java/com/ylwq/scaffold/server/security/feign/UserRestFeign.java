package com.ylwq.scaffold.server.security.feign;

import com.ylwq.scaffold.service.user.api.UserRestApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 用户服务接口调用<br/>
 * 技术要点：
 * 1. 需要在MainClass加上注解：`@EnableFeignClients`;<br/>
 * 2. 基于Springboot注册中心调用写法<br/>
 * `@FeignClient(value = "service-user")`<br/>
 * 3. 基于Kubernetes调用写法<br/>
 * `@FeignClient(name = "service-user", url = "http://user:7001`<br/>
 * url可以在放在配置中心配置，url = "${rpc.service.user.url}"<br/>
 *
 * @Author thymi
 * @Date 2021/3/9
 */
@FeignClient(value = "service-user")
public interface UserRestFeign extends UserRestApi {
}
