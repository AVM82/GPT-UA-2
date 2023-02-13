package com.group.gptua.service;

import com.group.gptua.model.UserSession;
import com.group.gptua.utils.NoFreeTokenException;

public interface UserSessionServiceInt {

  UserSession getUserSession(String userHash) throws NoFreeTokenException;

}
