package shop.mtcoding.bank.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.bank.config.dummy.DummyObject;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.user.UserReqDto.JoinReqDto;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
// 컨트롤러 테스트는 통합 테스트이다.
public class UserControllerTest extends DummyObject {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  public void setup() {
    dataSetting();
  }

  private void dataSetting() {
    userRepository.save(newUser("ssar", "쌀"));
  }

  @Test
  public void join_success_test() throws Exception {
    // given
    JoinReqDto joinReqDto = new JoinReqDto();
    joinReqDto.setUsername("love");
    joinReqDto.setPassword("1234");
    joinReqDto.setEmail("love@nate.com");
    joinReqDto.setFullname("러브");

    String requestBody = objectMapper.writeValueAsString(joinReqDto);
    // System.out.println("테스트 : " + requestBody);

    // when
    ResultActions resultActions = mvc
        .perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    // System.out.println("테스트 : " + responseBody);

    // then
    resultActions.andExpect(status().isCreated());

  }

  @Test
  public void join_fail_test() throws Exception {
    // given
    JoinReqDto joinReqDto = new JoinReqDto();
    joinReqDto.setUsername("ssar");
    joinReqDto.setPassword("1234");
    joinReqDto.setEmail("ssar@nate.com");
    joinReqDto.setFullname("쌀");

    String requestBody = objectMapper.writeValueAsString(joinReqDto);
    // System.out.println("테스트 : " + requestBody);

    // when
    ResultActions resultActions = mvc
        .perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println("테스트 : " + responseBody);

    // then
    resultActions.andExpect(status().isBadRequest());

  }

}
