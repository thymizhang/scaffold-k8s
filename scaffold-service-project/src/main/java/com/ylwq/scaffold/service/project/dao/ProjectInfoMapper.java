package com.ylwq.scaffold.service.project.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ylwq.scaffold.service.project.entity.ProjectInfo;
import org.springframework.stereotype.Repository;

/**
 * 项目信息数据访问对象
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Repository("projectInfoMapper")
public interface ProjectInfoMapper extends BaseMapper<ProjectInfo> {
}
