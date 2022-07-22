package online.toosimple.smartlog.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONUtil;
import online.toosimple.smartlog.SmartLogApplication;
import online.toosimple.smartlog.enums.ResultCode;
import online.toosimple.smartlog.exception.ServiceException;
import online.toosimple.smartlog.vo.GetRespVo;
import online.toosimple.smartlog.vo.PostReqVo;
import online.toosimple.smartlog.vo.R;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.mock;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SmartLogApplication.class})
class TestControllerTest {

    @Autowired
    private WebApplicationContext context;
    @MockBean
    TestController testController;

    MockMvc mockMvc;

    //junit4使用@Before
    //junit5使用@BeforeEach
    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testGetNoParam() throws Exception{
        GetRespVo vo = new GetRespVo();
        vo.setName("no name");
        vo.setAge(0);
        Mockito.when(testController.testGetNoParam()).thenReturn(vo); //放入假对象隔离service层

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/smart/log/get/noParam"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testGet() throws Exception{
        String name = "has name";
        Integer age = 18;

        GetRespVo vo = new GetRespVo();
        vo.setName(name);
        vo.setAge(age);
        Mockito.when(testController.testGet(name, age)).thenReturn(vo); //放入假对象隔离service层

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", name);
        params.add("age", age.toString());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/smart/log/get").params(params))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testPostNoArgs() throws Exception{
        GetRespVo vo = new GetRespVo();
        vo.setName("post name");
        vo.setAge(1);
        Mockito.when(testController.testPostNoArgs()).thenReturn(vo); //放入假对象隔离service层

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/smart/log/post/noArgs"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testPostForm() throws Exception {
        String form1 = "form1";
        Integer form2 = 12;

        GetRespVo vo = new GetRespVo();
        vo.setName(form1);
        vo.setAge(form2);
        Mockito.when(testController.testPostForm(form1, form2)).thenReturn(vo); //放入假对象隔离service层

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("form1", form1);
        params.add("form2", form2.toString());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/smart/log/post/form")
                .params(params)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testPostBody() throws Exception{
        String name = "body";
        Integer age = 12;

        GetRespVo vo = new GetRespVo(name, age);
        PostReqVo postReqVo = new PostReqVo(name, age);
        Mockito.when(testController.testPostBody(postReqVo)).thenReturn(vo); //放入假对象隔离service层

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/smart/log/post/body")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSON.toJSONString(postReqVo))
                    .characterEncoding(StandardCharsets.UTF_8.name()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testPostBodyReturnRSuccess() throws Exception {
        String name = "body";
        Integer age = 12;

        GetRespVo vo = new GetRespVo(name, age);
        PostReqVo postReqVo = new PostReqVo(name, age);

        R<GetRespVo> respVoR = R.data(ResultCode.SUCCESS, vo);
        respVoR.setMsgId("111222333");
        Mockito.doReturn(respVoR).when(testController).testPostBodyReturnRSuccess(postReqVo);   //返回泛型，要用这种写法

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/smart/log/post/body/return/r/success")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(postReqVo))
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        //Charset.defaultCharset()解决中文乱码问题
        log.info(mvcResult.getResponse().getContentAsString(Charset.defaultCharset()));
    }

    @Test
    void testPostBodyReturnRError() throws Exception{
        String name = "";
        Integer age = 12;

        GetRespVo vo = new GetRespVo(name, age);
        PostReqVo postReqVo = new PostReqVo(name, age);

        Mockito.doThrow(new ServiceException(ResultCode.FAILURE)).when(testController).testPostBodyReturnRError(postReqVo);

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/smart/log/post/body/return/r/error")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(postReqVo))
                        .characterEncoding(StandardCharsets.UTF_8.name()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        //Charset.defaultCharset()解决中文乱码问题
        log.info(mvcResult.getResponse().getContentAsString(Charset.defaultCharset()));
    }
}