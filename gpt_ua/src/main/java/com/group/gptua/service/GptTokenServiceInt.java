package com.group.gptua.service;

public interface GptTokenServiceInt {

  String getToken();

  void giveBackToken(String token);
}
