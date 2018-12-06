package org.upgrad.services;

import org.upgrad.models.Restaurant;
import org.upgrad.requestResponseEntity.RestaurantResponse;
import org.upgrad.requestResponseEntity.RestaurantResponseCategorySet;

import java.util.List;

public interface RestaurantService {
    List<RestaurantResponse> getAllRestaurant();

    List<RestaurantResponse> getRestaurantByName(String restaurantMatchingName);

    List<RestaurantResponse> getRestaurantByCategory(String categoryMacthingName);

    RestaurantResponseCategorySet getRestaurantDetails(Integer restaurantId);

    Restaurant updateRating(Double rating, Integer restaurantId);
}
