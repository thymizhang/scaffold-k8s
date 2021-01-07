package com.ylwq.scaffold.service.project.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ylwq.scaffold.service.project.entity.ProjectBudget;
import org.springframework.stereotype.Repository;

/**
 * 项目预算清单数据访问对象
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Repository("projectBudgetMapper")
public interface ProjectBudgetMapper extends BaseMapper<ProjectBudget> {
}
