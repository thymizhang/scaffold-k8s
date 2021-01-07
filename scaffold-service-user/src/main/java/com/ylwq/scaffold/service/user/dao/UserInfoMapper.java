package com.ylwq.scaffold.service.user.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ylwq.scaffold.service.user.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 用户信息数据访问对象<br/>
 * <p>
 * 1. BaseMapper提供基本的CRUD；
 * 2. 使用@Select标签，可自定义查询；
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Repository("userInfoMapper")
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 自定义查询：查询逻辑删除用户的id和name
     *
     * @return 删除用户的id和name
     */
    @Select("select id,name from user_info where is_deleted = 1")
    List<Map<String, Object>> selectDeleted();

    /**
     * 恢复逻辑删除<br/>
     * 技术要点：<br/>
     * 自定义update语句没有返回结果，没有异常就是执行成功
     */
    @Select("update user_info set is_deleted = 0 where is_deleted = 1")
    void reverse();

    /**
     * 自定义分页sql
     *
     * @param page    分页对象
     * @param wrapper 查询条件，例：LambdaQueryWrapper<UserDao> userLambdaQueryWrapper = Wrappers.lambdaQuery()
     * @return IPage
     */
    @Select("select * from user_info ${ew.customSqlSegment}")
    IPage<UserInfo> selectAllPage(Page<UserInfo> page, @Param(Constants.WRAPPER) Wrapper<UserInfo> wrapper);
}
