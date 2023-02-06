package com.group.gptua.repository;

import com.group.gptua.utils.Models;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public interface OpenAiInt {

  public String getTextMessage(Models model, String question);

  public List<Models> getModels();

}
