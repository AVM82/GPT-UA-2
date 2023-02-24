package com.group.gptua.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:test.properties")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OpenAiServiceTest {

  @InjectMocks
  OpenAiService openAiService;

  @Mock
  RequestDtoPropertiesService requestDtoPropertiesService;

  @Mock
  Environment environment;

  @Mock
  UserSession userSession;

  @Mock
  Response response;

  private final MediaType json = MediaType.get("application/json; charset=utf-8");

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
  void getTextMessage() throws IOException {
    String testToken = "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";
    String question = "test";
    when(userSession.getToken()).thenReturn(new GptToken(testToken, new GptAccount("testAccaunt")));
    when(response.isSuccessful()).thenReturn(true);
    assertEquals(openAiService.getTextMessage(userSession, Models.ADA, question), "test");
  }

  @Test
  void setMessage() {
  }
}