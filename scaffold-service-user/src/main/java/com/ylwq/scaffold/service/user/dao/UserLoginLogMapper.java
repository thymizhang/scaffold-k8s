package com.ylwq.scaffold.service.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ylwq.scaffold.service.user.entity.UserLoginLog;
import org.springframework.stereotype.Repository;

/**
 * 用户登录日志数据访问对象
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Repository("userLoginLogMapper")
public interface UserLoginLogMapper extends BaseMapper<UserLoginLog> {
}
