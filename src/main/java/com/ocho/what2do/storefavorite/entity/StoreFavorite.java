package com.ocho.what2do.storefavorite.entity;

import com.ocho.what2do.store.entity.Store;
import com.ocho.what2do.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 사용자 찜한 가게
@Entity
@Getter
@NoArgsConstructor
@Table(name="store_favorite")
public class StoreFavorite {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id")
  private Store store;

  public StoreFavorite(Store store, User user) {
    this.store = store;
    this.user = user;
  }
}

