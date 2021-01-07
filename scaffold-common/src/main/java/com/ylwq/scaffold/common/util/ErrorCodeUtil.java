package com.ylwq.scaffold.common.util;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * 错误码工具类<br/>
 * 配合自定义异常使用。
 *
 * @Author thymi
 * @Date 2021/1/7
 */
@Deprecated
public class ErrorCodeUtil {

    /**
     * 错误码文件名，默认保存位置：classes：/
     */
    private static final String ERROR_CODE_FILE = "error_code.properties";

    /**
     * 错误码信息
     */
    private static final Properties PROPERTIES = ErrorCodeUtil.getProperties();

    /**
     * 从文件读取错误码信息
     *
     * @return Properties
     */
    private static Properties getProperties() {
        Properties properties = null;
        try {
            properties = PropertiesLoaderUtils.loadAllProperties(ErrorCodeUtil.ERROR_CODE_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 根据错误码获取错误信息
     *
     * @param code 错误码
     * @return 错误信息
     */
    public static String getErrorInfo(String code) {
        return ErrorCodeUtil.PROPERTIES.getProperty(code);
    }
}
