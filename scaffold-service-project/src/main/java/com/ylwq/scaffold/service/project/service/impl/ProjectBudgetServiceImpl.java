package com.ylwq.scaffold.service.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ylwq.scaffold.service.project.dao.ProjectBudgetMapper;
import com.ylwq.scaffold.service.project.entity.ProjectBudget;
import com.ylwq.scaffold.service.project.service.ProjectBudgetService;
import org.springframework.stereotype.Service;

/**
 * 项目预算清单接口实现
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Service
public class ProjectBudgetServiceImpl extends ServiceImpl<ProjectBudgetMapper, ProjectBudget> implements ProjectBudgetService {
}
