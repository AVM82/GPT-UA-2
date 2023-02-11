package com.group.gptua.controllers;

import com.group.gptua.bot.Bot;
import com.group.gptua.dto.ApiDto;
import com.group.gptua.dto.DtoMessage;
import com.group.gptua.model.UserRequestEntity;
import com.group.gptua.repository.UserRequestRepository;
import com.group.gptua.service.OpenAiInt;
import com.group.gptua.service.OpenAiService;
import com.group.gptua.service.UserRequestService;
import com.group.gptua.utils.Models;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/archive")
@Slf4j
public class RequestController {

  UserRequestService requestService;

  public RequestController(UserRequestService requestService) {
    this.requestService = requestService;
  }

  /**
   * This is controller.
   *
   * @return list
   */
  @GetMapping("/{model}/{text}/{date}")
  public List<UserRequestEntity> getAllTasks(
      @PathVariable Models model,
      @PathVariable String text,
      @PathVariable LocalDate date) {

    List<UserRequestEntity> list = new ArrayList<>();
    requestService.getAll()
        .stream()
        .filter(x -> x.getModel().equals(model))
        .filter(x -> x.getRequest().contains(text))
        .filter(x -> x.getCreatedAt().toLocalDate().equals(date))
        .limit(100)
        .forEach(list::add);
    return list;
  }

  /**
   * This is controller.
   *
   * @return list
   */
  @GetMapping("/")
  public List<UserRequestEntity> getAllTasks() {

    List<UserRequestEntity> list = new ArrayList<>();
    return requestService.getAll();
  }
}

//ODM4MDA3NDM1e3x9aHR0cDovL2dwdHVhLWVudi5lYmEta213djh3cHQuZXUtY2VudHJhbC0xLmVsYXN0aWNiZWFuc3RhbGsuY29tL2NoYXR7fH1Nb3ppbGxhLzUuMChYMTE7VWJ1bnR1O0xpbnV4eDg2XzY0O3J2OjEwOS4wKUdlY2tvLzIwMTAwMTAxRmlyZWZveC8xMDkuMA==
//2023-02-10

//  List<UserRequestEntity> list = new ArrayList<>();
//        requestService.getAll()
//            .stream()
//            .filter(x -> x.getModel().equals(model))
//            .filter(x -> x.getRequest().contains(text))
//            .filter(x -> x.getCreatedAt().toLocalDate().equals(date))
//            .limit(100)
//            .forEach(list::add);
//            return list;