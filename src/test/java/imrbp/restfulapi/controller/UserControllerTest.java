package imrbp.restfulapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import imrbp.restfulapi.entity.User;
import imrbp.restfulapi.model.RegisterUserRequest;
import imrbp.restfulapi.model.WebResponse;
import imrbp.restfulapi.repository.UserRepository;
import imrbp.restfulapi.security.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
       userRepository.deleteAll();
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        RegisterUserRequest request= new RegisterUserRequest();
        request.setUsername("jhondoe");
        request.setName("Jhon Doe");
        request.setPassword("123456");

        mockMvc.perform(
               post("/api/users")
                       .accept(MediaType.APPLICATION_JSON)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo( result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});
            assertEquals("Ok", response.getData());
        });
    }

    @Test
    void testRegisterUserFailBadRequest() throws Exception {
        RegisterUserRequest request= new RegisterUserRequest();
        request.setUsername("");
        request.setName("");
        request.setPassword("");

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo( result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});
            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testRegisterUserFailDuplicate() throws Exception {
        User user = new User();
        user.setName("dummy user");
        user.setUsername("dummy");
        user.setPassword(BCrypt.hashpw("password123",BCrypt.gensalt()));
        userRepository.save(user);

        RegisterUserRequest request= new RegisterUserRequest();
        request.setUsername("dummy");
        request.setName("dummy user");
        request.setPassword("password123");

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo( result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {});
            assertNotNull(response.getErrors());
        });
    }
}