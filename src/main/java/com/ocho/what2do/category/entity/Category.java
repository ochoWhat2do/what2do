package com.ocho.what2do.category.entity;

import com.ocho.what2do.common.entity.Timestamped;
import com.ocho.what2do.storecategory.entity.StoreCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Category extends Timestamped {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @OneToMany(mappedBy = "category")
  private List<StoreCategory> storeCategoryList = new ArrayList<>();

  public Category(String name){
    this.name = name;
  }

  public Category(Long id, String name) {
    this.id = id;
    this.name =name;
  }
}
