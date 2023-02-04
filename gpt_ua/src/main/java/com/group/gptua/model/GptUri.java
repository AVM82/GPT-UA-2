package com.group.gptua.model;

public enum GptUri {
  BASE_URI("https://api.openai.com/"),
  URI_MODELS("https://api.openai.com/v1/models"),
  URI_MODEL("https://api.openai.com/v1/models/"),
  URI_COMPLETIONS("https://api.openai.com/v1/completions");
  private final String uri;

  GptUri(String uri) {
    this.uri = uri;
  }

  public String getUri() {
    return uri;
  }
}
