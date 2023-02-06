package com.group.gptua.utils;

public enum Models {
  BABBAGE("babbage"),
  ADA("ada"),
  CURIE("curie"),
  DAVINCI("davinci");

  private final String modelName;

  Models(String modelName) {
    this.modelName = modelName;
  }

  public String getModelName() {
    return modelName;
  }
}
