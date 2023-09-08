package com.ocho.what2do.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ocho.what2do.user.dto.LoginRequestDto;
import com.ocho.what2do.user.dto.SignupRequestDto;
import com.ocho.what2do.user.entity.User;
import com.ocho.what2do.user.repository.UserRepository;
import com.ocho.what2do.user.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@AutoConfigureMockMvc
public class UserServiceTest {

  @Autowired
  private UserServiceImpl userService;
  @Autowired
  private UserRepository userRepository;

  @Autowired
  ObjectMapper mapper;

  @Autowired
  MockMvc mvc;

  private static final String BASE_URL = "/api/users";

  @BeforeEach
  void setMasterInfo() {
    Optional<User> user1 = userRepository.findByEmail("test01@email.com");
    String email = "test01@email.com";
    String password = "test1234!";
    SignupRequestDto requestDto;
    if (!user1.isPresent()) {
      requestDto = SignupRequestDto.builder()
          .email(email).password(password).build();
      SignUp(requestDto);
    }

  }

  @Test
  @Order(1)
  @DisplayName("User 로그인")
  void login() throws Exception {
    // given
    String email = "test01@email.com";
    String password = "test1234!";

    // when
    LoginRequestDto requestDto = new LoginRequestDto();
    requestDto.setEmail(email);
    requestDto.setPassword(password);

    String body = mapper.writeValueAsString(requestDto);

    // then
    MvcResult result = mvc.perform(post(BASE_URL + "/login")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andReturn();
    Assertions.assertNotNull(Objects.requireNonNull(result.getResponse().getHeader("Authorization")));
    //Assertions.assertEquals("Bearer", Objects.requireNonNull(result.getResponse().getHeader("Authorization")).substring(0, 6));
  }


  @DisplayName("회원가입")
  void SignUp(SignupRequestDto signupRequestDto) {
    userService.signup(signupRequestDto);
  }

}
