package com.ocho.what2do.store.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocho.what2do.common.file.S3FileDto;
import com.ocho.what2do.store.dto.StoreResponseDto;
import com.ocho.what2do.store.entity.StoreCountEntity;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ocho.what2do.review.entity.QReview.review;
import static com.ocho.what2do.store.entity.QApiStore.apiStore;
import static com.ocho.what2do.store.entity.QStore.store;

@RequiredArgsConstructor
@Repository
public class StoreRepositoryImpl implements CustomStoreRepository {

  private final JPAQueryFactory queryFactory;

  private final ObjectMapper objectMapper;

  @Override
  public List<StoreResponseDto> findStoresListReview(Pageable pageable) {

    var subQuery = queryFactory
        .select(review.store.id.as("storeId"), review.count().as("reviewCount"))
        .from(review)
        .groupBy(review.store.id);

    // 리뷰 개수가 있음
    List<StoreCountEntity> storeContList = subQuery.stream().map(v ->
        new StoreCountEntity(v.get(0, Long.class), v.get(1, Long.class))
    ).toList();

    List<Long> storeIdList = storeContList.stream()
        .map(StoreCountEntity::getStoreId)
        .collect(Collectors.toList());

    // StoreCountEntity의 storeId와 reviewCount를 매핑하는 Map을 생성
    Map<Long, Long> storeIdToReviewCountMap = storeContList.stream()
        .collect(Collectors.toMap(StoreCountEntity::getStoreId, StoreCountEntity::getReviewCount));

    Expression<String> jsonImages = Expressions.stringTemplate("CAST({0} AS text)", apiStore.images).as("images");
    var query = queryFactory.select(
            store.id.as("is"),
            apiStore.storeKey.as("storeKey"),
            store.title.as("title"), // store 엔티티의 title 컬럼을 별칭 "title"로 지정
            store.homePageLink.as("homePageLink"),
            store.category.as("category"),
            store.address.as("address"), // store 엔티티의 address 컬럼을 별칭 "address"로 지정
            store.roadAddress.as("roadAddress"),
            store.latitude.as("latitude"),
            store.longitude.as("longitude"),
            store.viewCount.as("viewCount"),
            jsonImages // 타입을 명시
        )
        .from(apiStore)
        .join(store).on(apiStore.storeKey.eq(store.storeKey))
        .where(
            store.id.in(storeIdList)
        );

    var lists = query.fetch();

    List<StoreResponseDto> responseList = lists.stream()
        .map(row ->
            {
              try {
                return StoreResponseDto.builder()
                    .id(row.get(0, Long.class))
                    .storeKey(row.get(1, String.class))
                    .title(row.get(2, String.class))
                    .homePageLink(row.get(3, String.class))
                    .category(row.get(4, String.class))
                    .address(row.get(5, String.class))
                    .roadAddress(row.get(6, String.class))
                    .latitude(row.get(7, String.class))
                    .longitude(row.get(8, String.class))
                    .viewCount(row.get(9, Integer.class))
                    .images(
                        row.get(10, String.class) == null ? null :
                        objectMapper.readValue(row.get(10, String.class), new TypeReference<>() {
                        })
                    )
                    .reviewCount(storeIdToReviewCountMap.get(row.get(0, Long.class)))
                    .build();
              } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
              }
            }
        )
        .sorted(Comparator.comparingLong(StoreResponseDto::getReviewCount).reversed())
        .limit(pageable.getPageSize()) // 프론트에서 받아온 페이지당 개수만큼만 보여준다
        .collect(Collectors.toList());

    return responseList;
  }

  /**
   * OrderSpecifier 를 쿼리로 반환하여 정렬조건을 맞춰준다. 리스트 정렬
   *
   * @param page
   * @return
   */
  private OrderSpecifier<?> storeSort(Pageable page) {
    //서비스에서 보내준 Pageable 객체에 정렬조건 null 값 체크
    if (!page.getSort().isEmpty()) {
      //정렬값이 들어 있으면 for 사용하여 값을 가져온다
      for (Sort.Order order : page.getSort()) {
        // 서비스에서 넣어준 DESC or ASC 를 가져온다.
        Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
        // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅하여 준다.
        switch (order.getProperty()) {
          case "title":
            return new OrderSpecifier(direction, store.title);
          case "createdAt":
            return new OrderSpecifier(direction, store.createdAt);
        }
      }
    }
    return null;
  }
}
