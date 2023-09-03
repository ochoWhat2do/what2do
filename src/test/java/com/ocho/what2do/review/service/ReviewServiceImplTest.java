package com.ocho.what2do.review.service;

import com.ocho.what2do.admin.dto.AdminStoreRequestDto;
import com.ocho.what2do.admin.dto.AdminStoreResponseDto;
import com.ocho.what2do.admin.service.AdminStoreServiceImpl;
import com.ocho.what2do.common.exception.CustomException;
import com.ocho.what2do.review.dto.ReviewRequestDto;
import com.ocho.what2do.review.dto.ReviewResponseDto;
import com.ocho.what2do.review.entity.Review;
import com.ocho.what2do.review.repository.ReviewRepository;
import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.store.repository.StoreRepository;
import com.ocho.what2do.user.dto.SignupRequestDto;
import com.ocho.what2do.user.entity.User;
import com.ocho.what2do.user.repository.UserRepository;
import com.ocho.what2do.user.service.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReviewServiceImplTest {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ReviewServiceImpl reviewService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminStoreServiceImpl storeService;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private ReviewRepository reviewRepository;

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
            createStoreTest();
        }
    }

    @DisplayName("가게 생성")
    void createStoreTest() {
        //given
        String storeTitle1 = "TEST가게1";
        String homePageLink1 = "가게 홈페이지 주소 1";
        String category1 = "한식";
        String address1 = "테스트1 가게 주소";
        String roadAddress1 = "가게 도로명 주소 1";
        String latitude1 = "가게 x좌표 1";
        String longitude1 = "가게 y좌표 1";
        String storeKey = "-10";
        //when
        AdminStoreRequestDto requestDto1 = AdminStoreRequestDto.builder()
                .title(storeTitle1)
                .homePageLink(homePageLink1)
                .category(category1)
                .address(address1)
                .roadAddress(roadAddress1)
                .latitude(latitude1)
                .longitude(longitude1)
                .storeKey(storeKey)
                .build();
        MockMultipartFile file1 = null;
        try {
            file1 = new MockMultipartFile("files"
                    , "happy.png"
                    , "multipart/form-data"
                    , new FileInputStream(getClass().getResource("/image/happy.png").getFile())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MockMultipartFile file2 = null;
        try {
            file2 = new MockMultipartFile("files"
                    , "lol.png"
                    , "multipart/form-data"
                    , new FileInputStream(getClass().getResource("/image/lol.png").getFile())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<MultipartFile> files = List.of(
                file1,
                file2
        );

        //then
        User user = userRepository.findByEmail("test01@email.com").orElse(null);
        assertNotNull(user);
        AdminStoreResponseDto responseDto1 = storeService.createStore(requestDto1, user, files);
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

    @Test
    @Order(1)
    @DisplayName("리뷰 생성")
    void createReviewTest() throws IOException {

        //given
        String title = "타이틀1#";
        String content = "라뷰1";
        Long orderNo = 0L;
        String storeKey = "-10";
        List<Store> storeList = storeRepository.getStoreListByStoreKey(storeKey);
        Long storeId = (long) (storeList.size() > 0 ? storeList.get(storeList.size() - 1).getId() : -1);

        //when
        ReviewResponseDto responseDto = null;
        if (storeId > 0) {
            ReviewRequestDto requestDto = ReviewRequestDto.builder()
                    .title(title)
                    .content(content)
                    .orderNo(orderNo)
                    .storeId(storeId)
                    .build();
            MockMultipartFile file1 = new MockMultipartFile("files"
                    , "palmtree.png"
                    , "multipart/form-data"
                    , new FileInputStream(getClass().getResource("/image/palmtree.png").getFile())
            );
            MockMultipartFile file2 = new MockMultipartFile("files"
                    , "pororo.png"
                    , "multipart/form-data"
                    , new FileInputStream(getClass().getResource("/image/pororo.png").getFile())
            );

            List<MultipartFile> files = List.of(
                    file1,
                    file2
            );
            User user = userRepository.findByEmail("test01@email.com").orElse(null);
            responseDto = reviewService.createReview(storeId, requestDto, user, null);
        }

        //then
        if (responseDto != null) {

            assertNotNull(responseDto);
            assertNotNull(responseDto.getId());  // 리뷰 생성 후 ID 값이 있는지 확인
            assertEquals(title, responseDto.getTitle());
            assertEquals(content, responseDto.getContent());
        }
    }

    @Test
    @Order(2)
    @DisplayName("리뷰 삭제하기")
    void deleteReviewTest() throws IOException {

        // Given
        String title = "타이틀1#";
        String content = "라뷰1";
        Long orderNo = 0L;
        String storeKey = "-10";
        List<Store> storeList = storeRepository.getStoreListByStoreKey(storeKey);
        Long storeId = (long) (storeList.size() > 0 ? storeList.get(storeList.size() - 1).getId() : -1);
        String userEmail = "test01@email.com";


        User user = userRepository.findByEmail(userEmail).orElse(null);
        assertNotNull(user);
        ReviewResponseDto responseDto = null;
        if (storeId > 0) {
            ReviewRequestDto requestDto = ReviewRequestDto.builder()
                    .title(title)
                    .content(content)
                    .orderNo(orderNo)
                    .storeId(storeId)
                    .build();
            MockMultipartFile file1 = new MockMultipartFile("files"
                    , "palmtree.png"
                    , "multipart/form-data"
                    , new FileInputStream(getClass().getResource("/image/palmtree.png").getFile())
            );
            MockMultipartFile file2 = new MockMultipartFile("files"
                    , "pororo.png"
                    , "multipart/form-data"
                    , new FileInputStream(getClass().getResource("/image/pororo.png").getFile())
            );

            List<MultipartFile> files = List.of(
                    file1,
                    file2
            );
            reviewService.createReview(storeId, requestDto, user, null);
        }


        // When
        List<Review> reviews = reviewRepository.findByTitle(title);
        Long reviewId = reviews.get(reviews.size() - 1).getId();
        reviewService.deleteReview(reviewId, user);

        // Then
        assertThrows(CustomException.class, () -> reviewService.getReview(reviewId, user));
    }

    @DisplayName("회원가입")
    void SignUp(SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
    }
}

