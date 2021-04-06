import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 客户端Oauth2权限测试
 *
 * @Author thymi
 * @Date 2021/4/1
 */
public class ClientAuthTest {
    /**
     * 认证授权服务器url
     */
    public static String SERVER_AUTHORIZATION_URL = "http://localhost:8080";

    /**
     * 资源服务器url
     */
    public static String SERVER_RESOURCE_URL = "http://localhost:8081";

    /**
     * 网关服务器url
     */
    public static String SERVER_GATEWAY_URL = "http://localhost:9000";

    /**
     * 客户端id
     */
    public static String PARAM_NAME_CLIENT_ID = "client_id";

    /**
     * 客户端密码
     */
    public static String PARAM_NAME_CLIENT_SECRET = "client_secret";

    /**
     * 认证类型："authorization_code", "password", "client_credentials", "implicit", "refresh_token"
     */
    public static String PARAM_NAME_GRANT_TYPE = "grant_type";

    /**
     * 客户端回调地址，用于接收授权码
     */
    public static String PARAM_NAME_REDIRECT_URI = "redirect_uri";

    /**
     * 授权码
     */
    public static String PARAM_NAME_CODE = "code";

    /**
     * 用户名
     */
    public static String PARAM_NAME_USERNAME = "username";

    /**
     * 用户密码
     */
    public static String PARAM_NAME_PASSWORD = "password";

    /**
     * 令牌
     */
    public static String PARAM_NAME_TOKEN = "token";

    /**
     * 刷新令牌
     */
    public static String PARAM_NAME_REFRESH_TOKEN = "refresh_token";


    /**
     * 授权码模式获取令牌，需要先获取授权码
     * 授权码获取请求：http://localhost:8080/oauth/authorize?client_id=ios&response_type=code&scope=scope1&redirect_uri=/code
     */
    @Test
    public void getTokenByCode() {
        HashMap<String, Object> paramMap = new HashMap<>(5);
        paramMap.put(PARAM_NAME_CLIENT_ID, "scaffold_client_web");
        paramMap.put(PARAM_NAME_CLIENT_SECRET, "111111");
        paramMap.put(PARAM_NAME_GRANT_TYPE, "authorization_code");
        paramMap.put(PARAM_NAME_REDIRECT_URI, "/code");
        paramMap.put(PARAM_NAME_CODE, "4lYu8F");

        String post = HttpUtil.post(SERVER_AUTHORIZATION_URL + "/oauth/token", paramMap);
        System.out.println(post);
    }

    /**
     * 密码模式获取令牌
     */
    @Test
    public void getTokenByPassword() {
        HashMap<String, Object> paramMap = new HashMap<>(5);
        paramMap.put(PARAM_NAME_CLIENT_ID, "scaffold_client_web");
        paramMap.put(PARAM_NAME_CLIENT_SECRET, "111111");
        paramMap.put(PARAM_NAME_GRANT_TYPE, "password");
        paramMap.put(PARAM_NAME_USERNAME, "张三");
        paramMap.put(PARAM_NAME_PASSWORD, "111111");

        String post = HttpUtil.post(SERVER_GATEWAY_URL + "/oauth/token", paramMap);
        System.out.println(post);
    }

    /**
     * 客户端模式获取令牌
     */
    @Test
    public void getTokenByClient() {
        HashMap<String, Object> paramMap = new HashMap<>(3);
        paramMap.put(PARAM_NAME_CLIENT_ID, "scaffold_client_web");
        paramMap.put(PARAM_NAME_CLIENT_SECRET, "111111");
        paramMap.put(PARAM_NAME_GRANT_TYPE, "client_credentials");

        String post = HttpUtil.post(SERVER_AUTHORIZATION_URL + "/oauth/token", paramMap);
        System.out.println(post);
    }

    /**
     * 简单模式获取令牌
     */
    @Test
    public void getTokenBySimple() {
        /* 简单模式不需要写代码测试，直接使用以下请求在浏览器获取 */
        /* http://localhost:8080/oauth/authorize?client_id=scaffold_client_web&response_type=token&scope=scope1&redirect_uri=/code */
    }


    /**
     * 校验令牌
     */
    @Test
    public void checkToken() {
        HashMap<String, Object> paramMap = new HashMap<>(1);
        paramMap.put(PARAM_NAME_TOKEN, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsic2VydmljZS1wcm9qZWN0Iiwic2VydmljZS11c2VyIiwic2VydmljZS1jb21wYW55Il0sInVzZXJfaWQiOjMsInVzZXJfbmFtZSI6IuW8oOS4iSIsInNjb3BlIjpbInNjYWZmb2xkIiwic2NhZmZvbGRfYWRtaW4iXSwiZXhwIjoxNjE3MjY4NTQ5LCJhdXRob3JpdGllcyI6WyJhZG1pbiIsIm5vcm1hbCIsImd1ZXN0IiwiUk9MRV9BRE1JTiIsInRlc3QiLCJ1c2VyIl0sImp0aSI6Ijk2ZjIwNjRkLTNlMTMtNDRjMi05M2NkLTAyOTI2ZGJhYjA5YiIsImNsaWVudF9pZCI6InNjYWZmb2xkX2NsaWVudF93ZWIifQ.gnCxkKfqldX3ky8G_mU_BV1ZdcwrBna4381mfjXXjiY");

        String post = HttpUtil.post(SERVER_GATEWAY_URL + "/oauth/check_token", paramMap);
        System.out.println(post);
    }

    /**
     * 刷新令牌
     */
    @Test
    public void refreshToken() {
        HashMap<String, Object> paramMap = new HashMap<>(2);
        paramMap.put(PARAM_NAME_CLIENT_ID, "scaffold_client_web");
        paramMap.put(PARAM_NAME_CLIENT_SECRET, "111111");
        paramMap.put(PARAM_NAME_GRANT_TYPE, "refresh_token");
        paramMap.put(PARAM_NAME_REFRESH_TOKEN, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsic2VydmljZS1wcm9qZWN0Iiwic2VydmljZS11c2VyIiwic2VydmljZS1jb21wYW55Il0sInVzZXJfaWQiOjMsInVzZXJfbmFtZSI6IuW8oOS4iSIsInNjb3BlIjpbInNjYWZmb2xkIiwic2NhZmZvbGRfYWRtaW4iXSwiYXRpIjoiOTZmMjA2NGQtM2UxMy00NGMyLTkzY2QtMDI5MjZkYmFiMDliIiwiZXhwIjoxNjE3MjY4NjU3LCJhdXRob3JpdGllcyI6WyJhZG1pbiIsIm5vcm1hbCIsImd1ZXN0IiwiUk9MRV9BRE1JTiIsInRlc3QiLCJ1c2VyIl0sImp0aSI6IjllNjFiZTE4LWQzNWEtNGYwNS1hNjkyLTYyNjc4MWViOGRiNSIsImNsaWVudF9pZCI6InNjYWZmb2xkX2NsaWVudF93ZWIifQ.qZBBKf2-pJ1n8LPIs5QuT2mkW2Ia5oMt_GMxcTANUAc");

        String post = HttpUtil.post(SERVER_AUTHORIZATION_URL + "/oauth/token", paramMap);
        System.out.println(post);
    }

    /**
     * 通过token访问资源服务器
     */
    @Test
    public void getResourceByToken() {
        HashMap<String, Object> head = new LinkedHashMap<>(2);
        String body = HttpRequest.get(SERVER_GATEWAY_URL + "/api/company/user")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsic2VydmljZS1wcm9qZWN0Iiwic2VydmljZS11c2VyIiwic2VydmljZS1jb21wYW55Il0sInVzZXJfaWQiOjMsInVzZXJfbmFtZSI6IuW8oOS4iSIsInNjb3BlIjpbInNjYWZmb2xkIiwic2NhZmZvbGRfYWRtaW4iXSwiZXhwIjoxNjE3MjY5MTY4LCJhdXRob3JpdGllcyI6WyJhZG1pbiIsIm5vcm1hbCIsImd1ZXN0IiwiUk9MRV9BRE1JTiIsInRlc3QiLCJ1c2VyIl0sImp0aSI6IjQzNjRhZWNiLWYwYjgtNGE4Mi1hZWRlLTkxNjkzYjc3MjgxNCIsImNsaWVudF9pZCI6InNjYWZmb2xkX2NsaWVudF93ZWIifQ.MulBeih2nsftl3M-qSaTZlAh4unsaDWkikoxJWXheeU")
                .execute().body();
        System.out.println(body);
    }
}
