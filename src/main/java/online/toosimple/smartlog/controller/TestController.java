package online.toosimple.smartlog.controller;

import lombok.extern.slf4j.Slf4j;
import online.toosimple.smartlog.enums.ResultCode;
import online.toosimple.smartlog.exception.ServiceException;
import online.toosimple.smartlog.vo.GetRespVo;
import online.toosimple.smartlog.vo.PostReqVo;
import online.toosimple.smartlog.vo.R;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class TestController {

    //不带查询参数的get请求
    @GetMapping("/smart/log/get/noParam")
    public GetRespVo testGetNoParam() {
        GetRespVo vo = new GetRespVo();
        vo.setName("no name");
        vo.setAge(0);
        return vo;
    }

    //带参数的get请求
    @GetMapping("/smart/log/get")
    public GetRespVo testGet(@RequestParam String name, @RequestParam Integer age) {
        log.info("name: {}, age: {}", name, age);
        GetRespVo vo = new GetRespVo();
        vo.setName(name);
        vo.setAge(age);
        return vo;
    }

    //不带参数的post请求
    @PostMapping("/smart/log/post/noArgs")
    public GetRespVo testPostNoArgs() {
        GetRespVo vo = new GetRespVo();
        vo.setName("name");
        vo.setAge(1);
        return vo;
    }

    //参数是form-data的post请求
    @PostMapping("/smart/log/post/form")
    public GetRespVo testPostForm(String form1, Integer form2) {
        log.info("name: {}, age: {}", form1, form2);
        GetRespVo vo = new GetRespVo();
        vo.setName(form1);
        vo.setAge(form2);
        return vo;
    }

    //参数是java对象的post请求
    @PostMapping("/smart/log/post/body")
    public GetRespVo testPostBody(@RequestBody PostReqVo vo) {
        log.info("name: {}, age: {}", vo.getName(), vo.getAge());
        GetRespVo resp = new GetRespVo();
        resp.setName(vo.getName());
        resp.setAge(vo.getAge());
        return resp;
    }

    //使用R对象返回统一格式
    @PostMapping("/smart/log/post/body/return/r/success")
    public R<GetRespVo> testPostBodyReturnRSuccess(@RequestBody PostReqVo vo) {
        log.info("name: {}, age: {}", vo.getName(), vo.getAge());
        GetRespVo resp = new GetRespVo();
        resp.setName(vo.getName());
        resp.setAge(vo.getAge());
        return R.success(resp);
    }

    //抛出异常
    @PostMapping("/smart/log/post/body/return/r/error")
    public R<GetRespVo> testPostBodyReturnRError(@RequestBody PostReqVo vo) {
        log.info("name: {}, age: {}", vo.getName(), vo.getAge());
        if ("".equals(vo.getName())) {
            throw new ServiceException(ResultCode.FAILURE);
        }
        GetRespVo resp = new GetRespVo();
        resp.setName(vo.getName());
        resp.setAge(vo.getAge());
        return R.success(resp);
    }
}
