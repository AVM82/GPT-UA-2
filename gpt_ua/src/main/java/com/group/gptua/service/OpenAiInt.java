package com.group.gptua.service;

import com.group.gptua.model.UserSession;
import com.group.gptua.utils.Models;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public interface OpenAiInt {

  //String getTextMessage(Models model, String question);

  String getTextMessage(UserSession userSession, Models model, String question);

  List<Models> getModels();

}
