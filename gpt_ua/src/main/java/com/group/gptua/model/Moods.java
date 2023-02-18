package com.group.gptua.model;

public enum Moods {
  BUSINESS("business"),
  FRIENDLY("friendly"),
  ROUGH("rough"),
  CHILDREN("children");
  private final String moodName;

  Moods(String moodName) {
    this.moodName = moodName;
  }

  public String getMoodName() {
    return moodName;
  }
}
