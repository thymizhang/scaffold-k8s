package com.ylwq.scaffold.common.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Java对象工具类<br/>
 * 使用场景：Entity、Dto、Vo间信息复制
 *
 * @Author thymi
 * @Date 2021/1/7
 */
public class BeansUtil extends BeanUtils {

    /**
     * 将源对象集合转换为目标对象集合
     *
     * @param sources     源目标对象
     * @param targetClass 目标对象Class
     * @param <T>         需要返回的对象泛型
     * @return 返回的目标对象集合
     */
    public static <T> List<T> copyList(List sources, Class<T> targetClass) {
        List<T> targets = new ArrayList<>();
        if (sources == null) {
            return targets;
        }

        if (sources.size() > 0) {
            sources.forEach(item -> {
                try {
                    T target = targetClass.getDeclaredConstructor().newInstance();
                    BeansUtil.copyProperties(item, target);
                    targets.add(target);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return targets;
    }
}
