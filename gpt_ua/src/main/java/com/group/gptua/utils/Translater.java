package com.group.gptua.utils;

import com.group.gptua.dto.DtoMessage;

public interface Translater {

  DtoMessage translateToEnglish(DtoMessage message);

}
