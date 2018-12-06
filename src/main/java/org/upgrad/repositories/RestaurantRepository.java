package org.upgrad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.upgrad.models.Restaurant;
import org.upgrad.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Query(nativeQuery = true,value = "select * from RESTAURANT order by user_rating desc")
    Iterable<Restaurant> getAllRestaurantsOrderByRatings();

    @Query(nativeQuery = true,value = "select * from restaurant where restaurant_name iLIKE %?1% order by restaurant_name")
    Iterable<Restaurant> getRestaurantByMatchingName(String matchingRestaurantName);

    @Query(nativeQuery = true,value = "SELECT * from restaurant INNER JOIN restaurant_CATEGORY ON restaurant.id=restaurant_CATEGORY.restaurant_id WHERE restaurant_category.CATEGORY_ID= (select id from category where category_name iLIKE ?1)")
    Iterable<Restaurant> getRestaurantByCategory(String categoryName);

    @Query(nativeQuery = true,value = "select * from RESTAURANT where id=?1")
    Optional<Restaurant> getRestaurantById(Integer restaurantId);
}
