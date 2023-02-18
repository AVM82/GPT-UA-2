package com.group.gptua.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.group.gptua.dto.FilterParamDto;
import com.group.gptua.model.UserRequestEntity;
import com.group.gptua.repository.UserRequestRepository;
import com.group.gptua.utils.Models;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
class UserRequestServiceTest {

  @Mock
  UserRequestRepository repo;

  @InjectMocks
  UserRequestService service;

  List<UserRequestEntity> list;
  private static final Logger log = LoggerFactory.getLogger(UserRequestServiceTest.class);

  @Test
  void getFilteredRequests_withNullFilterParameters() {
    log.info("test getFilteredRequests_withNullFilterParameters starts...");
    FilterParamDto filterParam = new FilterParamDto();
    filterParam.setUserHash(null);
    filterParam.setModel(null);
    filterParam.setText(null);
    filterParam.setDateTo(null);
    filterParam.setDateFrom(null);

    assertEquals(service.getFilteredRequests(filterParam), list);
    log.info("test passed successfully.");
  }

  @Test
  void getFilteredRequests_withEmptyStrings_inFilterParameters() {
    log.info("test getFilteredRequests_withEmptyStrings_inFilterParameters starts...");
    FilterParamDto filterParam = new FilterParamDto();
    filterParam.setUserHash("");
    filterParam.setModel(null);
    filterParam.setText("");
    filterParam.setDateTo(null);
    filterParam.setDateFrom(null);

    assertEquals(service.getFilteredRequests(filterParam), list);
    log.info("test passed successfully.");
  }

  @Test
  void getFilteredRequests_withSingle_DateTo_inFilterParameters() {
    log.info("test getFilteredRequests_withSingle_DateTo_inFilterParameters starts...");
    FilterParamDto filterParam = new FilterParamDto();
    filterParam.setUserHash("");
    filterParam.setModel(null);
    filterParam.setText("");
    filterParam.setDateTo(LocalDate.parse("2023-02-12"));
    filterParam.setDateFrom(null);

    List<UserRequestEntity> userRequestList = service.getFilteredRequests(filterParam);

    assertEquals(1, userRequestList.size());
    log.info("size is ok...");
    assertEquals("5", userRequestList.get(0).getId());
    log.info("Id is ok...");
    log.info("test passed successfully.");
  }

  @Test
  void getFilteredRequests_withSingle_dateFrom_inFilterParameters() {
    log.info("test getFilteredRequests_withSingle_dateFrom_inFilterParameters starts...");
    FilterParamDto filterParam = new FilterParamDto();
    filterParam.setUserHash("");
    filterParam.setModel(null);
    filterParam.setText("");
    filterParam.setDateTo(null);
    filterParam.setDateFrom(LocalDate.parse("2023-02-12"));

    List<UserRequestEntity> userRequestList = service.getFilteredRequests(filterParam);

    assertEquals(1, userRequestList.size());
    log.info("size is ok...");
    assertEquals("5", userRequestList.get(0).getId());
    log.info("Id is ok...");
    log.info("test passed successfully.");
  }

  @Test
  void getFilteredRequests_withTwoEqualsDate_inFilterParameters() {
    log.info("test getFilteredRequests_withTwoEqualsDate_inFilterParameters starts...");
    FilterParamDto filterParam = new FilterParamDto();
    filterParam.setUserHash("");
    filterParam.setModel(null);
    filterParam.setText("");
    filterParam.setDateTo(LocalDate.parse("2023-02-12"));
    filterParam.setDateFrom(LocalDate.parse("2023-02-12"));

    List<UserRequestEntity> userRequestList = service.getFilteredRequests(filterParam);

    assertEquals(1, userRequestList.size());
    log.info("size is ok...");
    assertEquals("5", userRequestList.get(0).getId());
    log.info("Id is ok...");
    log.info("test passed successfully.");
  }

  @Test
  void getFilteredRequests_withTwoDifferentDate_inFilterParameters() {
    log.info("test etFilteredRequests_withTwoDifferentDate_inFilterParameters starts...");
    FilterParamDto filterParam = new FilterParamDto();
    filterParam.setUserHash("");
    filterParam.setModel(null);
    filterParam.setText("");
    filterParam.setDateTo(LocalDate.parse("2023-02-12"));
    filterParam.setDateFrom(LocalDate.parse("2023-02-10"));

    List<UserRequestEntity> userRequestList = service.getFilteredRequests(filterParam);

    assertEquals(3, userRequestList.size());
    log.info("size is ok...");
    assertEquals("5", userRequestList.get(0).getId());
    log.info("Id is ok...");
    assertEquals("4", userRequestList.get(1).getId());
    log.info("Id is ok...");
    assertEquals("3", userRequestList.get(2).getId());
    log.info("Id is ok...");
    log.info("test passed successfully.");
  }

  @Test
  void getFilteredRequests_byHash_inFilterParameters() {
    log.info("test getFilteredRequests_byHash_inFilterParameters starts...");
    FilterParamDto filterParam = new FilterParamDto();
    filterParam.setUserHash("123456789");
    filterParam.setModel(null);
    filterParam.setText("");
    filterParam.setDateTo(LocalDate.parse("2023-02-12"));
    filterParam.setDateFrom(LocalDate.parse("2023-02-08"));

    List<UserRequestEntity> userRequestList = service.getFilteredRequests(filterParam);

    assertEquals(3, userRequestList.size());
    log.info("size is ok...");
    assertEquals("3", userRequestList.get(0).getId());
    log.info("Id is ok...");
    assertEquals("2", userRequestList.get(1).getId());
    log.info("Id is ok...");
    assertEquals("1", userRequestList.get(2).getId());
    log.info("Id is ok...");
    log.info("test passed successfully.");
  }

  @Test
  void getFilteredRequests_byHash_byModel_inFilterParameters() {
    log.info("test getFilteredRequests_byHash_byModel_inFilterParameters starts...");
    FilterParamDto filterParam = new FilterParamDto();
    filterParam.setUserHash("123456789");
    filterParam.setModel(Models.ADA);
    filterParam.setText("");
    filterParam.setDateTo(LocalDate.parse("2023-02-12"));
    filterParam.setDateFrom(LocalDate.parse("2023-02-08"));

    List<UserRequestEntity> userRequestList = service.getFilteredRequests(filterParam);

    assertEquals(2, userRequestList.size());
    log.info("size is ok...");
    assertEquals("2", userRequestList.get(0).getId());
    log.info("Id is ok...");
    assertEquals("1", userRequestList.get(1).getId());
    log.info("Id is ok...");
    log.info("test passed successfully.");
  }

  @Test
  void getFilteredRequests_byHash_byModel_byText_inFilterParameters() {
    log.info("test getFilteredRequests_byHash_byModel_byText_inFilterParameters starts...");
    FilterParamDto filterParam = new FilterParamDto();
    filterParam.setUserHash("123456789");
    filterParam.setModel(Models.ADA);
    filterParam.setText("best");
    filterParam.setDateTo(LocalDate.parse("2023-02-12"));
    filterParam.setDateFrom(LocalDate.parse("2023-02-08"));

    List<UserRequestEntity> userRequestList = service.getFilteredRequests(filterParam);

    assertEquals(1, userRequestList.size());
    log.info("size is ok...");
    assertEquals("2", userRequestList.get(0).getId());
    log.info("Id is ok...");
    log.info("test passed successfully.");
  }

  @BeforeEach
  public void creatTestDB() {
    UserRequestEntity userRequest1 = new UserRequestEntity();
    userRequest1.setId("1");
    userRequest1.setCreatedAt(LocalDateTime.of(2023, 2, 8, 12, 10));
    userRequest1.setUserHash("123456789");
    userRequest1.setModel(Models.ADA);
    userRequest1.setRequest("What is the easiest way to make money?");
    userRequest1.setResponse("To earn a lot of money you have to work hard.");

    UserRequestEntity userRequest2 = new UserRequestEntity();
    userRequest2.setId("2");
    userRequest2.setCreatedAt(LocalDateTime.of(2023, 2, 9, 12, 10));
    userRequest2.setUserHash("123456789");
    userRequest2.setModel(Models.ADA);
    userRequest2.setRequest("What is the best programming language in the world?");
    userRequest2.setResponse("Java - is the best language.");

    UserRequestEntity userRequest3 = new UserRequestEntity();
    userRequest3.setId("3");
    userRequest3.setCreatedAt(LocalDateTime.of(2023, 2, 10, 12, 10));
    userRequest3.setUserHash("123456789");
    userRequest3.setModel(Models.BABBAGE);
    userRequest3.setRequest("Who is cooler than a java programmer?");
    userRequest3.setResponse(
        "Cooler than a java programmer is only a java programmer of a higher level.");

    UserRequestEntity userRequest4 = new UserRequestEntity();
    userRequest4.setId("4");
    userRequest4.setCreatedAt(LocalDateTime.of(2023, 2, 11, 12, 10));
    userRequest4.setUserHash("456789123");
    userRequest4.setModel(Models.CURIE);
    userRequest4.setRequest("What kind of programmers can chat replace?");
    userRequest4.setResponse("Chat will eventually replace all but Java backenders.");

    UserRequestEntity userRequest5 = new UserRequestEntity();
    userRequest5.setId("5");
    userRequest5.setCreatedAt(LocalDateTime.of(2023, 2, 12, 12, 10));
    userRequest5.setUserHash("567891234");
    userRequest5.setModel(Models.DAVINCI);
    userRequest5.setRequest("Who would be the envy of a Java professional of the highest class?");
    userRequest5.setResponse("Only to the junior, because he has all the victories yet to come.");

    list = new ArrayList<>();
    list.add(userRequest1);
    list.add(userRequest2);
    list.add(userRequest3);
    list.add(userRequest4);
    list.add(userRequest5);

    when(repo.findAll()).thenReturn(list);
  }
}