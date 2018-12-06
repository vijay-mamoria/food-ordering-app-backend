package org.upgrad.services;

import org.springframework.stereotype.Service;
import org.upgrad.common.Util;
import org.upgrad.models.Category;
import org.upgrad.models.Item;
import org.upgrad.models.Restaurant;
import org.upgrad.repositories.CategoryRepository;
import org.upgrad.repositories.ItemRepository;
import org.upgrad.repositories.RestaurantRepository;
import org.upgrad.repositories.UserRepository;
import org.upgrad.requestResponseEntity.CategoryResponse;
import org.upgrad.requestResponseEntity.RestaurantResponse;
import org.upgrad.requestResponseEntity.RestaurantResponseCategorySet;

import javax.persistence.OneToOne;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService{

    private final RestaurantRepository restaurantRepository;

    private final ItemRepository itemRepository ;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, ItemRepository itemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public List<RestaurantResponse> getAllRestaurant(){
        List<RestaurantResponse> restaurantResponsesList =  new ArrayList<RestaurantResponse>();
        Iterable<Restaurant> restaurants = restaurantRepository.getAllRestaurantsOrderByRatings();
        for(Restaurant restaurantDetails : restaurants){
            RestaurantResponse response = new RestaurantResponse(restaurantDetails.getId(),
                    restaurantDetails.getRestaurantName(),restaurantDetails.getPhotoUrl(),restaurantDetails.getUserRating(),restaurantDetails.getAvgPriceForTwo(),restaurantDetails.getNumberOfUsersRated(),restaurantDetails.getAddress(),restaurantDetails.getCategories().toString());
            List<Category> categories = restaurantDetails.getCategories();
            Collections.sort(categories, Util.categoryComparator);
            String categorys = Util.getInStringFormat(categories);
            response.setCategories(categorys);
            restaurantResponsesList.add(response);
        }
        return restaurantResponsesList;
    }

    @Override
    public List<RestaurantResponse> getRestaurantByName(String restaurantMatchingName){
        List<RestaurantResponse> restaurantResponsesList =  null;
        Iterable<Restaurant> restaurants = restaurantRepository.getRestaurantByMatchingName(restaurantMatchingName);
        for(Restaurant restaurantDetails : restaurants){
            if(restaurantResponsesList == null){
                restaurantResponsesList =  new ArrayList<RestaurantResponse>();
            }
            RestaurantResponse response = new RestaurantResponse(restaurantDetails.getId(),
                    restaurantDetails.getRestaurantName(),restaurantDetails.getPhotoUrl(),restaurantDetails.getUserRating(),restaurantDetails.getAvgPriceForTwo(),restaurantDetails.getNumberOfUsersRated(),restaurantDetails.getAddress(),restaurantDetails.getCategories().toString());
            List<Category> categories = restaurantDetails.getCategories();
            Collections.sort(categories, Util.categoryComparator);
            String categorys = Util.getInStringFormat(categories);
            response.setCategories(categorys);
            restaurantResponsesList.add(response);
        }
        return restaurantResponsesList;
    }

    @Override
    public  List<RestaurantResponse> getRestaurantByCategory(String categoryName){
        List<RestaurantResponse> restaurantResponsesList =  null;
        Iterable<Restaurant> restaurants = restaurantRepository.getRestaurantByCategory(categoryName);
        for(Restaurant restaurantDetails : restaurants){
            if(restaurantResponsesList == null){
                restaurantResponsesList =  new ArrayList<RestaurantResponse>();
            }
            RestaurantResponse response = new RestaurantResponse(restaurantDetails.getId(),
                    restaurantDetails.getRestaurantName(),restaurantDetails.getPhotoUrl(),restaurantDetails.getUserRating(),restaurantDetails.getAvgPriceForTwo(),restaurantDetails.getNumberOfUsersRated(),restaurantDetails.getAddress(),restaurantDetails.getCategories().toString());
            List<Category> categories = restaurantDetails.getCategories();
            Collections.sort(categories, Util.categoryComparator);
            String categorys = Util.getInStringFormat(categories);
            response.setCategories(categorys);
            restaurantResponsesList.add(response);
        }
        return restaurantResponsesList;
    }


    @Override
    public RestaurantResponseCategorySet getRestaurantDetails(Integer restaurantId){
        RestaurantResponseCategorySet restaurantResponseCategorySet =  null;
        Optional<Restaurant> restaurantObj = restaurantRepository.findById(restaurantId);
        if(restaurantObj.isPresent()){
            Restaurant restaurantDetails = restaurantObj.get();

            restaurantResponseCategorySet = new RestaurantResponseCategorySet(restaurantDetails.getId(),
                    restaurantDetails.getRestaurantName(),restaurantDetails.getPhotoUrl(),restaurantDetails.getUserRating(),restaurantDetails.getAvgPriceForTwo(),restaurantDetails.getNumberOfUsersRated(),restaurantDetails.getAddress(),null);
                    List<Category> categories = restaurantDetails.getCategories();
            Set<CategoryResponse> categorySet = new LinkedHashSet<>();
            Collections.sort(categories, Util.categoryComparator);

            for(Category category : categories) {
                        Set<Item> items = category.getItems();
                        CategoryResponse categoryResponse = new CategoryResponse(category.getId(), category.getCategoryName(), items);
                        categorySet.add(categoryResponse);
                    }

            restaurantResponseCategorySet.setCategories(categorySet);

        }
        return restaurantResponseCategorySet;
    }

    @Override
    public  Restaurant updateRating( Double rating, Integer restaurantId){
        Optional<Restaurant> restaurantObj = restaurantRepository.findById(restaurantId);
        if(restaurantObj.isPresent()){
            Restaurant restaurantDetails = restaurantObj.get();

            Integer numUsers = restaurantDetails.getNumberOfUsersRated();
            Integer newNumUsers = numUsers+1;
            restaurantDetails.setUserRating(rating);
            restaurantDetails.setNumberOfUsersRated(newNumUsers);
            restaurantRepository.save(restaurantDetails);
            return restaurantDetails;
        }
        return null;
    }


}
