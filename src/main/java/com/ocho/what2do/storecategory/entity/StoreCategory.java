package com.ocho.what2do.storecategory.entity;

import com.ocho.what2do.category.entity.Category;
import com.ocho.what2do.store.entity.Store;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@NoArgsConstructor
public class StoreCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  private Store store;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  private Category category;

  @CreatedDate
  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime createdAt;

  public StoreCategory(Store store, Category category){
    this.store = store;
    this.category = category;
  }

  public StoreCategory(Store store, String name){
    this.store = store;
    this.category = new Category(name);
  }
}
