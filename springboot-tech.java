
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
===
  package hello.controller;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.SpringBootTechApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootTechApplication.class)
@WebAppConfiguration
public abstract class AbstractTest {

    protected MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    protected void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        System.out.println("Before Each");
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}

===
  package hello.controller;

import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import hello.common.Result;
import hello.common.StatusType;
import hello.entity.Book;

public class BookControllerTest2 extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Test
    public void getAllBookTest() throws Exception {
        String uri = "/api/book/all";
        MvcResult mvcResult =
                mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        Assert.assertEquals(200, status);

        String content = response.getContentAsString();
        Result result = super.mapFromJson(content, Result.class);
        Assert.assertEquals(StatusType.SUCCESS.getCode(), result.getCode());

        List<Book> bookList = (List<Book>) result.getData();
        Assert.assertTrue(bookList.size() > 0);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getBooksTest_existData() throws Exception {
        String uri = "/api/book?page=0&size=10";
        MvcResult mvcResult =
                mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        Assert.assertEquals(200, status);

        String content = response.getContentAsString();
        Result result = super.mapFromJson(content, Result.class);
        Assert.assertEquals(StatusType.SUCCESS.getCode(), result.getCode());

        Map<Object, Object> data = (Map<Object, Object>) result.getData();
        Assert.assertTrue(Integer.valueOf(data.get("totalElements")+"") > 0);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getBooksTest_noData() throws Exception {
        String uri = "/api/book?page=0&size=10&searchText=thereisnobookcontainsthistext";
        MvcResult mvcResult =
                mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();
        Assert.assertEquals(200, status);

        String content = response.getContentAsString();
        Result result = super.mapFromJson(content, Result.class);
        Assert.assertEquals(StatusType.SUCCESS.getCode(), result.getCode());

        Map<Object, Object> data = (Map<Object, Object>) result.getData();
        Assert.assertTrue(Integer.valueOf(data.get("totalElements")+"") == 0);
    }
}
