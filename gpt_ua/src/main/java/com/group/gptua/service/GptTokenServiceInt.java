package com.group.gptua.service;

import com.group.gptua.model.GptToken;
import com.group.gptua.utils.NoFreeTokenException;

public interface GptTokenServiceInt {

  GptToken getToken() throws NoFreeTokenException;

  void giveBackToken(GptToken token) throws Exception;
}
