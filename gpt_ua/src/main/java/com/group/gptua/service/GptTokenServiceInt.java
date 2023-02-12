package com.group.gptua.service;

import com.group.gptua.model.GptToken;

public interface GptTokenServiceInt {

  GptToken getToken();

  void giveBackToken(GptToken token) throws Exception;
}
