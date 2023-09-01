package com.ocho.what2do.admin;

import com.ocho.what2do.admin.dto.AdminStoreRequestDto;
import com.ocho.what2do.admin.dto.AdminStoreResponseDto;
import com.ocho.what2do.admin.service.AdminStoreServiceImpl;
import com.ocho.what2do.store.repository.StoreRepository;
import com.ocho.what2do.user.dto.SignupRequestDto;
import com.ocho.what2do.user.entity.User;
import com.ocho.what2do.user.repository.UserRepository;
import com.ocho.what2do.user.service.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminStoreServiceImplTest {
  @Autowired
  private UserServiceImpl userService;
  @Autowired
  private AdminStoreServiceImpl storeService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private StoreRepository storeRepository;
  @BeforeEach
  void setMasterInfo() {
    Optional<User> user1 = userRepository.findByEmail("test01@email.com");
    String email = "test01@email.com";
    String password = "test1234!";
    SignupRequestDto requestDto;
    if (!user1.isPresent()) {
      requestDto = SignupRequestDto.builder()
          .email(email).password(password).build();
      SignUp(requestDto);
    }
  }
  @Test
  @Order(1)
  @DisplayName("가게 생성")
  void createStoreTest() throws IOException {
    //given
    String storeTitle1 = "TEST가게1";
    String homePageLink1 = "가게 홈페이지 주소 1";
    String category1 = "한식";
    String address1 = "테스트1 가게 주소";
    String roadAddress1 = "가게 도로명 주소 1";
    String latitude1 = "가게 x좌표 1";
    String longitude1 = "가게 y좌표 1";
    //when
    AdminStoreRequestDto requestDto1 = AdminStoreRequestDto.builder()
        .title(storeTitle1)
        .homePageLink(homePageLink1)
        .category(category1)
        .address(address1)
        .roadAddress(roadAddress1)
        .latitude(latitude1)
        .longitude(longitude1)
        .build();
//    MockMultipartFile file1 = new MockMultipartFile("files"
//            , "happy.png"
//            , "multipart/form-data"
//            , new FileInputStream(getClass().getResource("/image/happy.png").getFile())
//    );
//    MockMultipartFile file2 = new MockMultipartFile("files"
//            , "lol.png"
//            , "multipart/form-data"
//            , new FileInputStream(getClass().getResource("/image/lol.png").getFile())
//    );
//
//    List<MultipartFile> files = List.of(
//            file1,
//            file2
//    );
    //then
    User user = userRepository.findByEmail("test01@email.com").orElse(null);
    assertNotNull(user);
    AdminStoreResponseDto responseDto1 = storeService.createStore(requestDto1, user, null);
    assertNotNull(responseDto1);
    assertNotNull(responseDto1.getId());  // 가게 생성 후 ID 값이 있는지 확인
    assertEquals(storeTitle1, responseDto1.getTitle());
    assertEquals(homePageLink1, responseDto1.getHomePageLink());
    assertEquals(category1, responseDto1.getCategory());
    assertEquals(address1, responseDto1.getAddress());
    assertEquals(roadAddress1, responseDto1.getRoadAddress());
    assertEquals(latitude1, responseDto1.getLatitude());
    assertEquals(longitude1, responseDto1.getLongitude());
  }

//  @Test
//  @Order(2)
//  @DisplayName("가게 수정하기")
//  void updateStoreTest() {
//    // Given
//    AdminStoreRequestDto requestDto = AdminStoreRequestDto.builder()
//        .title("Old Title")
//        .homePageLink("Old Link")
//        .category("Old Category")
//        .address("Old Address")
//        .roadAddress("Old Road Address")
//        .latitude("Old Latitude")
//        .longitude("Old Longitude")
//        .build();
//    MockMultipartFile file1 = null;
//    try {
//      file1 = new MockMultipartFile("files"
//              , ".png"
//              , "multipart/form-data"
//              , new FileInputStream(getClass().getResource("/image/.png").getFile())
//      );
//    } catch (IOException e) {
//      throw new RuntimeException(e);
//    }
//    MockMultipartFile file2 = null;
//    try {
//      file2 = new MockMultipartFile("files"
//              , "lol.png"
//              , "multipart/form-data"
//              , new FileInputStream(getClass().getResource("/image/lol.png").getFile())
//      );
//    } catch (IOException e) {
//      throw new RuntimeException(e);
//    }
//
//    List<MultipartFile> files = List.of(
//            file1,
//            file2
//    );
//    User user = userRepository.findByEmail("test01@email.com").orElse(null);
//    assertNotNull(user);
//
//    AdminStoreResponseDto createdStore = storeService.createStore(requestDto, user, files);
//    Long storeId = createdStore.getId();
//
//    // When
//    AdminStoreRequestDto updateRequestDto = AdminStoreRequestDto.builder()
//        .title("Updated Title")
//        .homePageLink("Updated Link")
//        .category("Updated Category")
//        .address("Updated Address")
//        .roadAddress("Updated Road Address")
//        .latitude("Updated Latitude")
//        .longitude("Updated Longitude")
//        .build();
//
//
//    AdminStoreResponseDto updateStore = storeService.updateStore(storeId, updateRequestDto, user, files);
//
//    // Then
//    assertEquals(storeId, updateStore.getId());
//    assertEquals(updateRequestDto.getTitle(), updateStore.getTitle());
//    assertEquals(updateRequestDto.getHomePageLink(), updateStore.getHomePageLink());
//    assertEquals(updateRequestDto.getCategory(), updateStore.getCategory());
//    assertEquals(updateRequestDto.getAddress(), updateStore.getAddress());
//    assertEquals(updateRequestDto.getRoadAddress(), updateStore.getRoadAddress());
//    assertEquals(updateRequestDto.getLatitude(), updateStore.getLatitude());
//    assertEquals(updateRequestDto.getLongitude(), updateStore.getLongitude());
//
//  }

//  @Test
//  @Order(3)
//  @DisplayName("가게 아이디로 조회하기")
//  void getStoreByIdTest() {
//    // Given
//    AdminStoreRequestDto requestDto = AdminStoreRequestDto.builder()
//        .title("Test Store")
//        .homePageLink("Test Link")
//        .category("Test Category")
//        .address("Test Address")
//        .roadAddress("Test Road Address")
//        .latitude("Test Latitude")
//        .longitude("Test Longitude")
//        .build();
//    User user = userRepository.findByEmail("test01@email.com").orElse(null);
//    assertNotNull(user);
//    AdminStoreResponseDto createdStore = storeService.createStore(requestDto, user, files);
//    Long storeId = createdStore.getId();
//
//    // When
//    AdminStoreViewResponseDto storedStore = storeService.getStoreById(storeId, user);
//
//    // Then
//    assertEquals(storeId, storedStore.getId());
//    assertEquals("Test Store", storedStore.getTitle());
//    assertEquals("Test Link", storedStore.getHomePageLink());
//    assertEquals("Test Category", storedStore.getCategory());
//    assertEquals("Test Address", storedStore.getAddress());
//    assertEquals("Test Road Address", storedStore.getRoadAddress());
//
//  }
//
//  @Test
//  @Order(4)
//  @DisplayName("가게 삭제하기")
//  void deleteStoreTest() {
//    // Given
//    AdminStoreRequestDto requestDto = AdminStoreRequestDto.builder()
//        .title("Store to Delete")
//        .homePageLink("Delete Link")
//        .category("Delete Category")
//        .address("Delete Address")
//        .roadAddress("Delete Road Address")
//        .latitude("Delete Latitude")
//        .longitude("Delete Longitude")
//        .build();
//    User user = userRepository.findByEmail("test01@email.com").orElse(null);
//    assertNotNull(user);
//    AdminStoreResponseDto createdStore = storeService.createStore(requestDto, user, files);
//    Long storeId = createdStore.getId();
//
//    // When
//    storeService.deleteStore(storeId, user);
//
//    // Then
//    assertThrows(CustomException.class, () -> storeService.getStoreById(storeId, user));
//  }
  @DisplayName("회원가입")
  void SignUp(SignupRequestDto signupRequestDto) {
    userService.signup(signupRequestDto);
  }
}
