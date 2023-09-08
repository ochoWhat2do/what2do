//package com.ocho.what2do.admin;
//
//import com.ocho.what2do.admin.dto.AdminStoreRequestDto;
//import com.ocho.what2do.admin.dto.AdminStoreResponseDto;
//import com.ocho.what2do.admin.dto.AdminStoreViewResponseDto;
//import com.ocho.what2do.admin.service.AdminStoreServiceImpl;
//import com.ocho.what2do.common.exception.CustomException;
//import com.ocho.what2do.store.entity.Store;
//import com.ocho.what2do.store.repository.StoreRepository;
//import com.ocho.what2do.user.dto.SignupRequestDto;
//import com.ocho.what2do.user.entity.User;
//import com.ocho.what2do.user.repository.UserRepository;
//import com.ocho.what2do.user.service.UserServiceImpl;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.Assert.*;
//
//@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class AdminStoreServiceImplTest {
//    @Autowired
//    private UserServiceImpl userService;
//    @Autowired
//    private AdminStoreServiceImpl storeService;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private StoreRepository storeRepository;
//
//    @BeforeEach
//    void setMasterInfo() {
//        Optional<User> user1 = userRepository.findByEmail("test01@email.com");
//        String email = "test01@email.com";
//        String password = "test1234!";
//        SignupRequestDto requestDto;
//        if (!user1.isPresent()) {
//            requestDto = SignupRequestDto.builder()
//                    .email(email).password(password).build();
//            SignUp(requestDto);
//        }
//    }
//
//    @Test
//    @Order(1)
//    @DisplayName("가게 생성")
//    void createStoreTest() throws IOException {
//        //given
//        String storeTitle1 = "TEST가게1";
//        String storeKey1 = "-11";
//        String homePageLink1 = "가게 홈페이지 주소 1";
//        String category1 = "한식";
//        String address1 = "테스트1 가게 주소";
//        String roadAddress1 = "가게 도로명 주소 1";
//        String latitude1 = "가게 x좌표 1";
//        String longitude1 = "가게 y좌표 1";
//        //when
//        AdminStoreRequestDto requestDto1 = AdminStoreRequestDto.builder()
//                .title(storeTitle1)
//                .homePageLink(homePageLink1)
//                .category(category1)
//                .address(address1)
//                .roadAddress(roadAddress1)
//                .latitude(latitude1)
//                .longitude(longitude1)
//                .storeKey(storeKey1)
//                .build();
//        MockMultipartFile file1 = new MockMultipartFile("files"
//                , "happy.png"
//                , "multipart/form-data"
//                , new FileInputStream(getClass().getResource("/image/happy.png").getFile())
//        );
//        MockMultipartFile file2 = new MockMultipartFile("files"
//                , "lol.png"
//                , "multipart/form-data"
//                , new FileInputStream(getClass().getResource("/image/lol.png").getFile())
//        );
//
//        List<MultipartFile> files = List.of(
//                file1,
//                file2
//        );
//        //then
//        User user = userRepository.findByEmail("test01@email.com").orElse(null);
//        assertNotNull(user);
//        AdminStoreResponseDto responseDto1 = storeService.createStore(requestDto1, user, files);
//        assertNotNull(responseDto1);
//        assertNotNull(responseDto1.getId());  // 가게 생성 후 ID 값이 있는지 확인
//        assertEquals(storeTitle1, responseDto1.getTitle());
//        assertEquals(homePageLink1, responseDto1.getHomePageLink());
//        assertEquals(category1, responseDto1.getCategory());
//        assertEquals(address1, responseDto1.getAddress());
//        assertEquals(roadAddress1, responseDto1.getRoadAddress());
//        assertEquals(latitude1, responseDto1.getLatitude());
//        assertEquals(longitude1, responseDto1.getLongitude());
//        assertEquals(storeKey1, responseDto1.getStoreKey());
//    }
//
//    @Test
//    @Order(2)
//    @DisplayName("가게 수정하기")
//    void updateStoreTest() {
//        // Given
//        AdminStoreRequestDto updateRequestDto = AdminStoreRequestDto.builder()
//                .title("Updated Title")
//                .homePageLink("Updated Link")
//                .category("Updated Category")
//                .address("Updated Address")
//                .roadAddress("Updated Road Address")
//                .latitude("Updated Latitude")
//                .longitude("Updated Longitude")
//                .build();
//        MockMultipartFile file1 = null;
//        try {
//            file1 = new MockMultipartFile("files"
//                    , "happy.png"
//                    , "multipart/form-data"
//                    , new FileInputStream(getClass().getResource("/image/happy.png").getFile())
//            );
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        MockMultipartFile file2 = null;
//        try {
//            file2 = new MockMultipartFile("files"
//                    , "lol.png"
//                    , "multipart/form-data"
//                    , new FileInputStream(getClass().getResource("/image/lol.png").getFile())
//            );
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        List<MultipartFile> files = List.of(
//                file1,
//                file2
//        );
//        User user = userRepository.findByEmail("test01@email.com").orElse(null);
//        assertNotNull(user);
//        String storeKey1 = "-11";
//
//
//        // When
//
//
//        List<Store> storeList = storeRepository.getStoreListByStoreKey(storeKey1);
//        Long storeId = storeList.get(storeList.size() - 1).getId();
//        AdminStoreResponseDto updateStore = storeService.updateStore(storeId, updateRequestDto, user, files);
//
//        // Then
//        assertEquals(storeId, updateStore.getId());
//        assertEquals(updateRequestDto.getTitle(), updateStore.getTitle());
//        assertEquals(updateRequestDto.getHomePageLink(), updateStore.getHomePageLink());
//        assertEquals(updateRequestDto.getCategory(), updateStore.getCategory());
//        assertEquals(updateRequestDto.getAddress(), updateStore.getAddress());
//        assertEquals(updateRequestDto.getRoadAddress(), updateStore.getRoadAddress());
//        assertEquals(updateRequestDto.getLatitude(), updateStore.getLatitude());
//        assertEquals(updateRequestDto.getLongitude(), updateStore.getLongitude());
//
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("가게 아이디로 조회하기")
//    void getStoreByIdTest() {
//        // Given
//        String storeKey1 = "-11";
//        String updatedTitle = "Updated Title";
//        String updatedCategory = "Updated Category";
//        String updatedHomepage = "Updated Link";
//        String updatedAddress = "Updated Address";
//        String updatedRoadAddress = "Updated Road Address";
//        String updatedLatitude = "Updated Latitude";
//        String updatedLongitude = "Updated Longitude";
//        // When
//        List<Store> storeList = storeRepository.getStoreListByStoreKey(storeKey1);
//        Store findStore = storeService.findStore(storeList.get(storeList.size() - 1).getId());
//        AdminStoreResponseDto storedStore = new AdminStoreResponseDto(findStore);
//
//        // Then
//        assertEquals(storeKey1, storedStore.getStoreKey());
//        assertEquals(updatedTitle, storedStore.getTitle());
//        assertEquals(updatedCategory, storedStore.getCategory());
//        assertEquals(updatedHomepage, storedStore.getHomePageLink());
//        assertEquals(updatedAddress, storedStore.getAddress());
//        assertEquals(updatedRoadAddress, storedStore.getRoadAddress());
//        assertEquals(updatedLatitude, storedStore.getLatitude());
//        assertEquals(updatedLongitude, storedStore.getLongitude());
//    }
//
//    @Test
//    @Order(4)
//    @DisplayName("가게 삭제하기")
//    void deleteStoreTest() {
//        // Given
//        User user = userRepository.findByEmail("test01@email.com").orElse(null);
//        assertNotNull(user);
//        String storeKey1 = "-11";
//        List<Store> storeList = storeRepository.getStoreListByStoreKey(storeKey1);
//        Long storeId = storeList.get(storeList.size() - 1).getId();
//        // When
//        storeService.deleteStore(storeId, user);
//
//        // Then
//        assertThrows(CustomException.class, () -> storeService.getStoreById(storeId, user));
//    }
//
//    @DisplayName("회원가입")
//    void SignUp(SignupRequestDto signupRequestDto) {
//        userService.signup(signupRequestDto);
//    }
//}
