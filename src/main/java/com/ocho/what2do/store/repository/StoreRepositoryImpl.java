package com.ocho.what2do.store.repository;

import com.ocho.what2do.store.dto.StoreResponseDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ocho.what2do.common.daum.entity.QApiStore.apiStore;
import static com.ocho.what2do.review.entity.QReview.review;
import static com.ocho.what2do.store.entity.QStore.store;

@RequiredArgsConstructor
@Repository
public class StoreRepositoryImpl implements CustomStoreRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<StoreResponseDto> findStoresListReview(Pageable pageable) {
        var query = queryFactory.select(store)
                .from(apiStore)
                .join(store).on(apiStore.storeKey.eq(store.storeKey))
                .join(store.reviews, review)
                .where(
                        review.id.isNotNull()
                ).offset(pageable.getOffset())
                .limit(pageable.getPageSize())
//                .orderBy(storeSort(pageable), store.reviews.size())
                .orderBy(store.reviews.size().desc());

        var lists = query.fetch();

        List<StoreResponseDto> responseList = lists.stream().map(
                StoreResponseDto::new
        ).toList();

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
