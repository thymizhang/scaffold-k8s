package com.ylwq.scaffold.service.company.feign;

import com.ylwq.scaffold.service.user.api.UserRestApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 用户服务接口调用<br/>
 * 技术要点：
 * 1. 需要在MainClass加上注解：`@EnableFeignClients`;<br/>
 * 2. 基于Springboot注册中心调用写法<br/>
 * `@FeignClient(name = "${service.user.provider.name}")`<br/>
 * 3. 基于Kubernetes调用写法<br/>
 * `@FeignClient(name = "${service.user.provider.name}", url = "${service.user.provider.url}")`<br/>
 * name和url在配置文件中配置<br/>
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@FeignClient(name = "${service.user.provider.name}", url = "${service.user.provider.url}")
public interface UserRestFeign extends UserRestApi {
}
