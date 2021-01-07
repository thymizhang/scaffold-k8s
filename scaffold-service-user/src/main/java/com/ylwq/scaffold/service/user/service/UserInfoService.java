package com.ylwq.scaffold.service.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ylwq.scaffold.service.user.entity.UserInfo;

/**
 * 用户信息接口<br/>
 * service与mapper的区别：<br/>
 * 1. service与mapper是两个层面的接口；
 * 2. mapper是实体对象与数据库的基本CRUD操作；
 * 3. service在mapper基础上封装了更丰富的数据访问，包括批量处理等；
 *
 * @Author thymi
 * @Date 2021/1/7
 */
public interface UserInfoService extends IService<UserInfo> {
}
