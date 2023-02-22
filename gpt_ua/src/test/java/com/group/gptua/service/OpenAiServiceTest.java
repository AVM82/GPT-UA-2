package com.group.gptua.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.group.gptua.model.Moods;
import com.group.gptua.utils.Models;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpenAiServiceTest {

  OpenAiService openAiService;

  @BeforeEach
  void init() {
    this.openAiService = new OpenAiService();
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
  void getTextMessage() {
  }

  @Test
  void setMessage() {
  }
}