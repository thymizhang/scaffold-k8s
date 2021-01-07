package com.ylwq.scaffold.service.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ylwq.scaffold.service.user.dao.UserLoginLogMapper;
import com.ylwq.scaffold.service.user.entity.UserLoginLog;
import com.ylwq.scaffold.service.user.service.UserLoginLogService;
import org.springframework.stereotype.Service;

/**
 * 用户登录日志接口实现类
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Service
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogMapper, UserLoginLog> implements UserLoginLogService {
}
