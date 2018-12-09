package org.upgrad.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upgrad.models.Restaurant;
import org.upgrad.requestResponseEntity.RestaurantResponse;
import org.upgrad.requestResponseEntity.RestaurantResponseCategorySet;
import org.upgrad.services.RestaurantService;
import org.upgrad.services.UserAuthTokenService;
import org.upgrad.services.UserService;

import java.util.List;
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserAuthTokenService userAuthTokenService;

    /**
     * This end point is used to fetch the restaurant by name
     * @param reastaurantName
     * @return Restaurant details
     */

    @GetMapping("/name/{restaurantName}")
    @CrossOrigin
    public ResponseEntity<?> getRestaurantsByName(@PathVariable("restaurantName") String reastaurantName){
        List<RestaurantResponse> resResponseList = restaurantService.getRestaurantByName(reastaurantName);
        if(resResponseList == null || resResponseList.size()==0){
           return new ResponseEntity<>("No Restaurant by this name!",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(resResponseList, HttpStatus.OK);
    }

    /**
     * This endpoint is used to fetch the restaurant by category name
     * @param categoryName
     * @return Restaurnat details
     */

    @GetMapping("/category/{categoryName}")
    @CrossOrigin
    public ResponseEntity<?> getRestaurantByCategory(@PathVariable("categoryName") String categoryName){
        List<RestaurantResponse> resResponseList = restaurantService.getRestaurantByCategory(categoryName);
        if(resResponseList == null || resResponseList.size()==0){
            return new ResponseEntity<>("No Restaurant under this category!",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(resResponseList, HttpStatus.OK);
    }

    /**
     * This endpoint is used to fetch the restaurant by Id
     * @param restaurantId
     * @return Restaurnat details
     */
    @GetMapping("/{restaurantId}")
    @CrossOrigin
    public ResponseEntity<?> getRestaurantById(@PathVariable("restaurantId") Integer restaurantId){
        RestaurantResponseCategorySet restaurantResponseCategorySet = restaurantService.getRestaurantDetails(restaurantId);
        if(restaurantResponseCategorySet == null ){
            return new ResponseEntity<>("No Restaurant by this id!",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(restaurantResponseCategorySet, HttpStatus.OK);
    }

    /**
     * This end point is used to update the user rating for the restaurant.
     * Authentication is required to access this endpoint, so accessToken is taken as request header to make sure user is authenticated.
     * @param rating
     * @param restaurantId
     * @param accessToken
     * @return restaurant details
     */
    @PutMapping("/{restaurantId}")
    @CrossOrigin
    public ResponseEntity<?> updateRestaurantUserRating(@RequestParam("rating") Double rating, @PathVariable Integer restaurantId, @RequestHeader String accessToken){
        if(userAuthTokenService.isUserLoggedIn(accessToken) == null){
            return new ResponseEntity<>("Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        }
        else if(userAuthTokenService.isUserLoggedIn(accessToken).getLogoutAt()!=null){
            return new ResponseEntity<>("You have already logged out. Please Login first to access this endpoint!", HttpStatus.UNAUTHORIZED);
        }  else{
            Restaurant restaurant = restaurantService.updateRating(rating,restaurantId);;
            if(restaurant == null ){
                return new ResponseEntity<>("No Restaurant by this id!",HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(restaurant, HttpStatus.OK);

        }
    }

    /**
     * This end point is used to get all the restaurants
     * @return all restaurants
     */
    @GetMapping("")
    @CrossOrigin
    public ResponseEntity<?> getAllRestaurants() {
        List<RestaurantResponse> resResponseList = restaurantService.getAllRestaurant();
        return new ResponseEntity<>(resResponseList, HttpStatus.OK);
    }



}
