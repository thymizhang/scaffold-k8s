import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.ylwq.scaffold.common.exception.UserErrorCode;
import com.ylwq.scaffold.common.util.BeansUtil;
import com.ylwq.scaffold.common.util.JsonUtil;
import com.ylwq.scaffold.service.user.UserApplication;
import com.ylwq.scaffold.service.user.dao.UserInfoMapper;
import com.ylwq.scaffold.service.user.entity.UserInfo;
import com.ylwq.scaffold.service.user.service.UserInfoService;
import com.ylwq.scaffold.service.user.dto.ComboDto;
import com.ylwq.scaffold.service.user.dto.UserInfoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 用户信息服务测试
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {UserApplication.class})
public class UserInfoServiceTest {

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserInfoMapper userInfoMapper;
    /**
     * 测试用户id
     */
    Long userId = 1L;

    /**
     * 测试公司id
     */
    Long companyId = 1L;


    @Test
    public void save() {
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setUserName("刘一");
        userInfo1.setCompanyId(this.companyId);
        userInfo1.setPhone("18611111111");
        userInfo1.setPassword("123456");
        UserInfo userInfo2 = new UserInfo();
        userInfo2.setUserName("陈二");
        userInfo2.setCompanyId(this.companyId);
        userInfo2.setPhone("18622222222");
        userInfo2.setPassword("123456");
        UserInfo userInfo3 = new UserInfo();
        userInfo3.setUserName("张三");
        userInfo3.setCompanyId(this.companyId);
        userInfo3.setPhone("18633333333");
        userInfo3.setPassword("123456");
        UserInfo userInfo4 = new UserInfo();
        userInfo4.setUserName("李四");
        userInfo4.setCompanyId(this.companyId);
        userInfo4.setPhone("18644444444");
        userInfo4.setPassword("654321");
        UserInfo userInfo5 = new UserInfo();
        userInfo5.setUserName("王五");
        userInfo5.setPhone("18655555555");
        userInfo5.setPassword("111111");
        UserInfo userInfo6 = new UserInfo();
        userInfo6.setUserName("赵六");
        userInfo6.setPhone("18666666666");
        userInfo6.setPassword("111111");
        if (userInfoService.save(userInfo1)) {
            System.out.println("用户：" + userInfo1.getUserName() + " 创建成功。");
        }
        if (userInfoService.save(userInfo2)) {
            System.out.println("用户：" + userInfo2.getUserName() + " 创建成功。");
        }
        if (userInfoService.save(userInfo3)) {
            System.out.println("用户：" + userInfo3.getUserName() + " 创建成功。");
        }
        if (userInfoService.save(userInfo4)) {
            System.out.println("用户：" + userInfo4.getUserName() + " 创建成功。");
        }
        if (userInfoService.save(userInfo5)) {
            System.out.println("用户：" + userInfo4.getUserName() + " 创建成功。");
        }
        if (userInfoService.save(userInfo6)) {
            System.out.println("用户：" + userInfo4.getUserName() + " 创建成功。");
        }
    }

    @Test
    public void saveOrUpdate() {
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(UserInfo::getUserName, "周八");
        UserInfo user = userInfoService.getOne(lambdaQueryWrapper);
        UserInfo userNew = new UserInfo();
        if (user != null) {
            /* 有id时，更新 */
            System.out.println("将要更新的对象：" + JsonUtil.objectToJson(user));
            userNew.setId(user.getId());
            userNew.setPassword("111111");
        } else {
            /* 没有id时保存 */
            userNew.setUserName("周八");
            userNew.setPhone("13588888888");
            userNew.setPassword("123456");
        }
        boolean b = userInfoService.saveOrUpdate(userNew);
        System.out.println("保存或更新状态：" + b);
    }

    /**
     * 读取Nacos中的配置内容</br>
     * 方法一：通过@Value("${xxx.xxx}")可以获取任何一个配置文件中的属性值；</br>
     * 方法二：使用Nacos的Java SDK读取；</br>
     * 注意：从Nacos中的配置内容，包含了注释等信息，如果需要使用配置数据，需要将String转为Properties</br>
     */
    @Test
    public void nacosConfig() {
        String content = "";
        try {
            String serverAddr = "nacos.gceasy.cc:8848";
            String dataId = "combo.properties";
            String group = "SCAFFOLD";
            String nameSpace = "SCAFFOLD-DEV";
            Properties properties = new Properties();
            properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
            properties.put(PropertyKeyConst.NAMESPACE, nameSpace);
            ConfigService configService = NacosFactory.createConfigService(properties);
            content = configService.getConfig(dataId, group, 5000);
            System.out.println("配置文件内容：" + content);

        } catch (NacosException e) {
            e.printStackTrace();
        }

        Properties pt = new Properties();
        try {
            InputStream is = new ByteArrayInputStream(content.getBytes());
            pt.load(is);
            System.out.println("配置信息：" + pt.getProperty("C0321"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试错误码
     */
    @Test
    public void code() {
        System.out.println(System.getProperty("file.encoding"));
        System.out.println(HttpStatus.OK.name());
        System.out.println(HttpStatus.BAD_REQUEST);
        System.out.println(UserErrorCode.A0001);
    }

    /**
     * 读取本地properties配置文件
     */
    @Test
    public void localConfig() {
        Properties properties;
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("error_code.properties");
            System.out.println(JsonUtil.objectToJson(properties));
            System.out.println(properties.getProperty("C0321"));
            System.out.println(properties.get("A0001"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void entity2Dto() {
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(UserInfo::getCompanyId, "1");
        List<UserInfo> users = userInfoService.list(lambdaQueryWrapper);
        List<UserInfoDto> userInfoDtos = BeansUtil.copyList(users, UserInfoDto.class);
        System.out.println(userInfoDtos);
    }

    /**
     * 从配置文件读取json，然后转换成对象
     */
    @Test
    public void testCombo() {
        ComboDto comboDto1 = new ComboDto();
        comboDto1.setId("1");
        comboDto1.setMemberSize("2");
        comboDto1.setProjectSize("5");
        comboDto1.setDataSize("10G");
        ComboDto comboDto2 = new ComboDto();
        comboDto2.setId("1");
        comboDto2.setMemberSize("2");
        comboDto2.setProjectSize("5");
        comboDto2.setDataSize("10G");

        List<ComboDto> list = Lists.newArrayList();
        list.add(comboDto1);
        list.add(comboDto2);

        System.out.println(JsonUtil.objectToJson(list));
    }


    /**
     * 技术要点：QueryWrapper与LambdaQueryWrapper的区别
     */
    @Test
    public void count() {
        int count = userInfoService.count();
        System.out.println("总记录数：" + count);
        /* 条件统计 */
        QueryWrapper<UserInfo> queryWrapper = Wrappers.query();
        queryWrapper.nested(qw -> qw.eq("name", "张三").or().eq("name", "李四")).likeLeft("phone", "2");
        /* 建议使用LambdaQueryWrapper，可以避免出现硬编码 */
        LambdaQueryWrapper<UserInfo> queryWrapper1 = Wrappers.lambdaQuery();
        queryWrapper1.nested(qw -> qw.eq(UserInfo::getUserName, "张三").or().eq(UserInfo::getUserName, "李四")).likeLeft(UserInfo::getPhone, "2");
        int count1 = userInfoService.count(queryWrapper1);
        System.out.println("查询记录数：" + count1);
    }

    /**
     * 对象转换
     */
    @Test
    public void getById() {
        UserInfo userInfo = userInfoService.getById(this.userId);
        if (userInfo != null) {
            UserInfoDto userInfoDto = new UserInfoDto();
            BeanUtils.copyProperties(userInfo, userInfoDto);

            String json1 = JsonUtil.objectToJson(userInfo);
            System.out.println(userInfo);
            System.out.println("json1格式：" + json1);
            String json2 = JsonUtil.objectToJson(userInfoDto);
            System.out.println(userInfoDto);
            System.out.println("json2格式：" + json2);
        } else {
            System.out.println("没有找到数据 ...");
        }
    }

    /**
     * 技术要点：<br/>
     * 通过list.stream().map(UserInfo::getId).collect(Collectors.toList())得到一个属性集合，比如：id
     */
    @Test
    public void list() {
        List<UserInfo> list = userInfoService.list();
        System.out.println("列出全部：");
        list.forEach(System.out::println);
        /* 根据id集合查询 */
        List<Long> collect = list.stream().map(UserInfo::getId).collect(Collectors.toList());
        List<UserInfo> userInfos = userInfoService.listByIds(collect);
        System.out.println("根据id查询：");
        userInfos.forEach(item -> System.out.println(JsonUtil.objectToJson(item)));
    }

    /**
     * 技术要点：<br/>
     * and括号or条件组合：LambdaQueryWrapper.and(lqw->lqw.eq().or().eq())<br/>
     * or括号and条件组合：LambdaQueryWrapper.or(lqw->lqw.like().and().eq())
     */
    @Test
    public void listByLambdaWrapper() {
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        /* and括号or条件，大于等于 */
        lambdaQueryWrapper.and(lqw -> lqw.between(UserInfo::getId, 10, 20).or().ge(UserInfo::getId, 30));
        /* or括号and条件 */
        lambdaQueryWrapper.or(lqw -> lqw.notLike(UserInfo::getPhone, "188").like(UserInfo::getCompanyId, "186"));
        lambdaQueryWrapper.ge(UserInfo::getCreateTime, "2020-11-30");
        lambdaQueryWrapper.orderByDesc(UserInfo::getId);
        List<UserInfo> list = userInfoService.list(lambdaQueryWrapper);
        list.forEach(item -> System.out.println(JsonUtil.objectToJson(item)));
    }

    @Test
    public void update() {
        /* 对象单独更新 */
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setId(this.userId);
        userInfo1.setPhone("18811111111");
        boolean b = userInfoService.updateById(userInfo1);
        System.out.println("更新状态：" + b);
        /* 对象批量更新 */
        userInfo1.setPassword("000000");
        UserInfo userInfo2 = new UserInfo();
        userInfo2.setId(this.userId + 1);
        userInfo2.setPassword("111111");
        List<UserInfo> userInfos = Lists.newArrayList();
        userInfos.add(userInfo1);
        userInfos.add(userInfo2);
        boolean b1 = userInfoService.updateBatchById(userInfos);
        System.out.println("批量更新状态：" + b1);
    }

    @Test
    public void updateByLambdaWrapper() {
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(UserInfo::getPassword, "123456");
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword("888888");
        boolean update = userInfoService.update(userInfo, lambdaQueryWrapper);
        System.out.println("条件更新状态：" + update);
    }

    /**
     * 如果存在is_delete字段，则为逻辑删除，MybatisPlus并未提供恢复逻辑删除的方法
     */
    @Test
    public void remove() {
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(UserInfo::getUserName, "刘一");
        boolean remove = userInfoService.remove(lambdaQueryWrapper);
        System.out.println("删除状态：" + remove);
    }

    /**
     * 恢复被逻辑删除的数据
     */
    @Test
    public void reverse() {
        userInfoMapper.reverse();
    }

    /**
     * 技术要点：<br/>
     * 1. 分组统计目前只能使用QueryWrapper；<br/>
     * 2. 查询方法使用listMaps；<br/>
     * 3. 返回结果格式List<Map<String, Object>>；
     */
    @Test
    public void groupBy() {
        /* 统计相同密码的用户数量 */
        QueryWrapper<UserInfo> queryWrapper = Wrappers.query();
        queryWrapper.select("password, count(*) as pwdCount"); /* 自定义查询列 */
        queryWrapper.gt("id", 0);
        queryWrapper.groupBy("password");
        queryWrapper.having("count(*) > 1");
        queryWrapper.orderByAsc("password");

        List<Map<String, Object>> maps = userInfoService.listMaps(queryWrapper);
        maps.forEach(map -> System.out.println(JsonUtil.objectToJson(map)));
    }

    /**
     * 分页自定义查询
     */
    @Test
    public void customByPage() {
        /* isSearchCount为true，会多执行一次count(*)查询 */
        Page<UserInfo> userInfoPage = new Page<>(2, 4, true);
        LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.between(UserInfo::getCreateTime, "2020-01-01", "2020-12-31");
        IPage<UserInfo> userInfoIPage = userInfoMapper.selectAllPage(userInfoPage, lambdaQueryWrapper);
        System.out.println("总页数：" + userInfoIPage.getPages());
        System.out.println("总记录数：" + userInfoIPage.getTotal());
        System.out.println("当前页码：" + userInfoIPage.getCurrent());
        System.out.println("每页记录数：" + userInfoIPage.getSize());
        List<UserInfo> records = userInfoIPage.getRecords();
        records.forEach(record -> System.out.println(JsonUtil.objectToJson(record)));
    }

    /**
     * 自定义字段查询
     */
    @Test
    public void customByMap() {
        List<Map<String, Object>> maps = userInfoMapper.selectDeleted();
        maps.forEach(map -> System.out.println(JsonUtil.objectToJson(map)));
    }
}
