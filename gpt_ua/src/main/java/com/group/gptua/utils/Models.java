package com.group.gptua.utils;

public enum Models {
  BABBAGE("text-babbage-001"),
  ADA("text-ada-001"),
  CURIE("text-curie-001"),
  DAVINCI("text-davinci-003");

  private final String modelName;

  Models(String modelName) {
    this.modelName = modelName;
  }

  public String getModelName() {
    return modelName;
  }
}
