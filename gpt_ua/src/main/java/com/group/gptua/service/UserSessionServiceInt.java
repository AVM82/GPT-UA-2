package com.group.gptua.service;

import com.group.gptua.model.UserSession;

public interface UserSessionServiceInt {

  UserSession getUserSession(String userHash);

}
