# smart-log
use log4j2 record request and response

# 目的

更方便的查看业务日志，定位问题

# 功能

1. 通过aop，打印请求和响应内容
2. 统一返回格式
3. 生成traceId

# 例子

请求接口

```
curl -H 'Content-Type:application/json' \n
-d '{"name":"body", "age":21}' \n
-X POST \n
127.0.0.1:8080/smart/log/post/body/return/r/success

```

接口响应
```
{
    "code": 200,
    "msg": "操作成功",
    "msgId": "0163AA9F2E3F44AB9A743E78BAA76C4C",
    "data": {
        "name": "body",
        "age": 21
    }
}
```

日志输出
```
2022-07-22 14:55:14.402 [INFO][http-nio-8080-exec-1] msgId:[0163AA9F2E3F44AB9A743E78BAA76C4C] online.toosimple.smartlog.aspectj.RequestLogAop - request start: method: [POST], url: [/smart/log/post/body/return/r/success], params: [null], body: [{"name":"body","age":21}]
2022-07-22 14:55:14.412 [INFO][http-nio-8080-exec-1] msgId:[0163AA9F2E3F44AB9A743E78BAA76C4C] online.toosimple.smartlog.controller.TestController - this is log in controller method
2022-07-22 14:55:14.432 [INFO][http-nio-8080-exec-1] msgId:[0163AA9F2E3F44AB9A743E78BAA76C4C] online.toosimple.smartlog.aspectj.RequestLogAop - response: [{"code":200,"data":{"age":21,"name":"body"},"msg":"操作成功","msgId":"0163AA9F2E3F44AB9A743E78BAA76C4C"}], time consuming: [53 ms]
```

如果接口异常，根据对方提供的msgId，即可查询到请求日志。
