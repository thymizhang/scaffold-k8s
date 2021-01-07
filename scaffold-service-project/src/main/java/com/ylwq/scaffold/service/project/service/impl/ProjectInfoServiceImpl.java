package com.ylwq.scaffold.service.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ylwq.scaffold.service.project.dao.ProjectInfoMapper;
import com.ylwq.scaffold.service.project.entity.ProjectInfo;
import com.ylwq.scaffold.service.project.service.ProjectInfoService;
import org.springframework.stereotype.Service;

/**
 * 项目信息接口实现类
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Service
public class ProjectInfoServiceImpl extends ServiceImpl<ProjectInfoMapper, ProjectInfo> implements ProjectInfoService {
}
