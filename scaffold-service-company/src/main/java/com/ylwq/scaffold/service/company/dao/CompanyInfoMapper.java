package com.ylwq.scaffold.service.company.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ylwq.scaffold.service.company.entity.CompanyInfo;
import org.springframework.stereotype.Repository;

/**
 * 公司信息数据访问对象
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Repository("companyInfoMapper")
public interface CompanyInfoMapper extends BaseMapper<CompanyInfo> {
}
