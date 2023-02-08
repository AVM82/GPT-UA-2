package com.group.gptua.service;

import java.util.Objects;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Slf4j
public class RequestDtoPropertiesService {

  @Autowired
  Environment environment;

  public int getMaxTokens() {
    return Integer.parseInt(Objects.requireNonNull(environment.getProperty("gpt.max_tokens")));
  }

  public double getTopP() {
    return Double.parseDouble(Objects.requireNonNull(environment.getProperty("gpt.top_p")));
  }

  public double getTemperature() {
    return Double.parseDouble(Objects.requireNonNull(environment.getProperty("gpt.temperature")));
  }

  public double getFrequencyPenalty() {
    return Double.parseDouble(
        Objects.requireNonNull(environment.getProperty("gpt.frequency_penalty")));
  }

  public double getPresencePenalty() {
    return Double.parseDouble(
        Objects.requireNonNull(environment.getProperty("gpt.presence_penalty")));
  }

  public String[] getStop() {
    return new String[]{Objects.requireNonNull(environment.getProperty("gpt.stop"))};
  }
}
