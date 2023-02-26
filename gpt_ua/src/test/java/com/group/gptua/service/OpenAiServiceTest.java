package com.group.gptua.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.group.gptua.dto.ApiDto;
import com.group.gptua.dto.ApiWithMoodDto;
import com.group.gptua.model.GptAccount;
import com.group.gptua.model.GptToken;
import com.group.gptua.model.Moods;
import com.group.gptua.model.UserSession;
import com.group.gptua.utils.Models;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OpenAiServiceTest {

  @InjectMocks
  public OpenAiService openAiService;

  @Mock
  public UserSession userSession;

  public static MockWebServer server;

  public MockResponse response;

  private final MediaType jsonTest = MediaType.get("application/json; charset=utf-8");

  @BeforeAll
  static void setUp() throws IOException {
    server = new MockWebServer();
    server.start();
  }

  @BeforeEach
  public void init() {
    this.response = new MockResponse()
        .addHeader("Content-Type", "application/json; charset=utf-8")
        .setBody("test");
    server.enqueue(response);
  }

  @AfterAll
  static void tearDown() throws IOException {
    server.shutdown();
  }

  @Test
  void getModelsReturnCorrectValues() {
    List<Models> expected = Arrays.asList(Models.values());
    List<Models> actual = openAiService.getModels();
    assertEquals(expected, actual);
  }

  @Test
  void getMoodsReturnCorrectValues() {
    List<Moods> expected = Arrays.asList(Moods.values());
    List<Moods> actual = openAiService.getMoods();
    assertEquals(expected, actual);
  }

  @Test
  void setMessage() {
    String message = "test";
    ApiWithMoodDto apiWithMoodDto = new ApiWithMoodDto(Models.ADA, message, Moods.FRIENDLY);
    ApiDto expected = openAiService.setMessage(apiWithMoodDto);
    String question =
        "Paraphrase this text in a " + Moods.FRIENDLY.getMoodName() + " style : " + message;
    ApiDto actual = new ApiDto(Models.ADA, question);
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  void createResponseTest() {
    when(userSession.getToken()).thenReturn(new GptToken("token", new GptAccount("account")));
    RequestBody requestBody = RequestBody.create("test", jsonTest);
    Request request = openAiService.createPostRequest(userSession,
        "http:localhost:" + server.getPort(), requestBody);
    String expected = openAiService.createResponse(request);
    String actual = "test";
    assertEquals(expected, actual);
  }
}