package com.ylwq.scaffold.service.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ylwq.scaffold.service.user.entity.UserInfo;
import com.ylwq.scaffold.service.user.dao.UserInfoMapper;
import com.ylwq.scaffold.service.user.service.UserInfoService;
import org.springframework.stereotype.Service;

/**
 * 用户信息接口实现类
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
}
