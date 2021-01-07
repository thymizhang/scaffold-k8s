## 项目说明
公共项目，包括：各种工具类、异常定义、基础配置、统一返回对象等等。

## 技术要点
1. 自定义异常和错误码；
2. 统一返回状态码规则；
3. 全局异常处理；

### 自定义异常和错误码
* 参考：《Java开发手册》将异常分为A类（用户异常）、B类（系统异常）和C类（第三方异常）
1. 基于[`BaseException`](src/main/java/com/ylwq/scaffold/common/exception/BaseException.java)构建自定义运行时异常，包括：`UserException`、`SystemException`、`ClientException`；
2. `UserException`：对应A类异常，抛出该类异常时应给出A类[错误码](src/main/resources/error_code.properties)和异常信息；
3. `SystemException`：对应B类异常，抛出该类异常时应给出B类[错误码](src/main/resources/error_code.properties)和异常信息；
4. `ClientException`：对应C类异常，抛出该类异常时应给出C类[错误码](src/main/resources/error_code.properties)和异常信息；
* 抛出自定义异常代码参考：
```java
        if (user.equals(type)) {
            throw new UserException(UserErrorCode.A0001);
        }
        if (sys.equals(type)) {
            throw new SystemException(SystemErrorCode.B0001);
        }
        if (client.equals(type)) {
            throw new ClientException(ClientErrorCode.C0001);
        }
```
* 捕获并打印异常信息代码参考：
```java
        catch (UserException e) {
            e.printStackTrace();
            return ResponseDataUtil.buildFaild(e.getErrorDesc());
        } catch (ClientException e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getErrorDesc());
        } catch (SystemException e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getErrorDesc());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDataUtil.buildError(e.getMessage());
        }
```

### 统一返回状态码规则
1. 使用统一对象对前端返回状态码，参考[`ResponseData`](src/main/java/com/ylwq/scaffold/common/vo/ResponseData.java)；
2. 采用HttpStatus状态码：200（OK,请求成功）、400（BAD_REQUEST,请求失败）、500（INTERNAL_SERVER_ERROR，系统异常）；
3. [错误码](src/main/resources/error_code.properties)规范来自[《Java开发手册》](../resource/pdf/Java开发手册-嵩山版.pdf),返回状态码与异常错误码对应关系如下：

| 返回状态码 | 异常错误码 |
| :--------: | :--------: |
|    400     |    A类     |
|    500     |  B类、C类  |
参考：[Http状态码](https://www.runoob.com/http/http-status-codes.html)

### 全局异常处理
* 对自定义运行时异常和系统异常进行统一处理，减少try-catch代码，所有的微服务都需要添加一个异常处理类`GlobalControllerExceptionHandler`
```java
@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData handleUserException(UserException e) {
        e.printStackTrace();
        return new ResponseData(ResultEnums.FAILD, e.getErrorDesc());
    }

    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData handleSystemException(SystemException e) {
        e.printStackTrace();
        return new ResponseData(ResultEnums.FAILD, e.getErrorDesc());
    }

    @ExceptionHandler(ClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData handleClientException(ClientException e) {
        e.printStackTrace();
        return new ResponseData(ResultEnums.FAILD, e.getErrorDesc());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData handleException(Exception e) {
        e.printStackTrace();
        return new ResponseData(ResultEnums.ERROR, e.getMessage());
    }
}
```