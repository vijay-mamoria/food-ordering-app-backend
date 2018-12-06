package org.upgrad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.upgrad.models.Category;
import org.upgrad.models.Restaurant;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(nativeQuery = true,value = "SELECT id,category_name FROM CATEGORY")
    List<Category> getCategories();

    @Query(nativeQuery = true,value = "SELECT * FROM CATEGORY where category_name iLike ?1")
    Optional<Category> getCategoryByName(String categoryName);
}
