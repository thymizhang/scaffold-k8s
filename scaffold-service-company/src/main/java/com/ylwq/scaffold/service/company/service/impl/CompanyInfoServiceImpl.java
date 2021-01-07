package com.ylwq.scaffold.service.company.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ylwq.scaffold.service.company.dao.CompanyInfoMapper;
import com.ylwq.scaffold.service.company.entity.CompanyInfo;
import com.ylwq.scaffold.service.company.service.CompanyInfoService;
import org.springframework.stereotype.Service;

/**
 * 公司信息接口实现类
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Service
public class CompanyInfoServiceImpl extends ServiceImpl<CompanyInfoMapper, CompanyInfo> implements CompanyInfoService {
}
