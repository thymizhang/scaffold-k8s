package com.ylwq.scaffold.service.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录日志实体类<br/>
 * 技术要点：<br/>
 * 对应的数据表采用MySQL的Archive引擎，只支持insert和select操作，可以实现高并发的插入，支持在自增id上建立索引。
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Data
public class UserLoginLog {
    /**
     * 日志id
     */
    @TableId(type = IdType.AUTO)
    Long id;

    /**
     * 用户id
     */
    Long userId;

    /**
     * 登录ip地址
     */
    String ip;

    /**
     * 登录时间
     */
    String loginTime;
}
