package com.ylwq.scaffold.service.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息实体类<br/>
 * ORM三部曲：<br/>
 * 1. 创建实体对象，绑定表字段；<br/>
 * 2. 创建Mapper对象，定义基本CRUD；<br/>
 * 3. 创建Service，编写业务逻辑；<br/>
 * 4. 创建Test，测试Service；
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Data
public class UserInfo {
    /**
     * 用户id
     * <p>
     * 如果使用数据库自增长，需要配置@TableId(type = IdType.AUTO)<br/>
     * 如果使用sharding-jdbc发布式id的雪花算法，去掉配置@TableId<br/>
     * 主键策略:ASSIGN_ID:数值，雪花算法 ASSIGN_UUID:字符串
     */
    @TableId(type = IdType.AUTO)
    Long id;

    /**
     * 公司id
     */
    Long companyId;

    /**
     * 用户名<br/>
     * 当对象属性名与数据库字段名不一致时，使用@TableField绑定字段
     */
    @TableField(value = "name")
    String userName;

    /**
     * 用户头像<br/>
     * 符合驼峰规则，自动绑定字段，字段：image_head
     */
    String imageHead;

    /**
     * 手机号码，也是登录账号<br/>
     * condition: 使用实体作为查询条件时,设置条件匹配的方式,默认是eq(等于)
     */
    @TableField(condition = SqlCondition.LIKE)
    String phone;

    /**
     * 登录密码<br/>
     */
    String password;

    /**
     * 是否删除<br/>
     * 为MyBatisPlus配置逻辑删除<br/>
     * 1 在yml中配置logic-not-delete-value和logic-delete-value<br/>
     * 2 在字段配置@TableLogic(value = "0", delval = "1")，查询条件会自带is_delete='0'，注意:对自定义sql语句无效<br/>
     * 3 在字段配置@TableField(select = false)，查询不返回is_deleted字段，属性值为null<br/>
     * 4 数据库is_deleted字段需要设置默认值为0<br/>
     */
    @TableLogic(value = "0", delval = "1")
    String isDeleted;

    /**
     * 创建时间<br/>
     * 使用@TableField(fill = FieldFill.INSERT): 在插入时自动填充时间<br/>
     * 注意需要配置MetaObjectHandler实现<br/>
     * 时间类的自动填充建议使用数据库设置：CURRENT_TIMESTAMP<br/>
     * LocalDateTime格式存储到redis需要指定序列化方式@JsonFormat、@JsonSerialize、@JsonDeserialize
     */
    String createTime;

    /**
     * 更新时间<br/>
     * 使用@TableField(fill = FieldFill.INSERT_UPDATE): 在更新时自动填充时间，注意需要配置MetaObjectHandler实现<br/>
     * 时间类的自动填充建议使用数据库设置：`DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` <br/>
     * LocalDateTime格式存储到redis需要指定序列化方式<br/>
     */
    String updateTime;

    /**
     * 备注<br/>
     * 有些信息用于返回给前端,不是数据库的字段,有三种方式<br/>
     * 1 private transient ,缺点:不能序列化;<br/>
     * 2 private static , 缺点:静态化需要单独写get set;<br/>
     * 3 @TableField(exist = false) , 推荐.
     */
    @TableField(exist = false)
    String remark;
}
