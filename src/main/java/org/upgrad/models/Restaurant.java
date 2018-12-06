package org.upgrad.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "restaurant_name", nullable = false)
    private String restaurantName;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name="user_rating" , nullable=false)
    private Double userRating;

    @Column(name="average_price_for_two" , nullable=false)
    private Integer avgPrice;

    @Column(name="number_of_users_rated" , nullable=false)
    private Integer numberUsersRated;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId")
    private Address address;


    @ManyToMany(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "restaurant_item", joinColumns = @JoinColumn(name = "itemId"), inverseJoinColumns = @JoinColumn(name = "restaurantId"))
    private List<Item> items =  new ArrayList<Item>();


   @ManyToMany(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "restaurant_category", joinColumns = @JoinColumn(name = "restaurantId"), inverseJoinColumns = @JoinColumn(name = "categoryId"))

   /* @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")*/
    private List<Category> categories = new ArrayList<Category>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Double getUserRating() {
        return userRating;
    }

    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }

    public Integer getAvgPriceForTwo() {
        return avgPrice;
    }

    public void setAvgPriceForTwo(Integer avgPriceForTwo) {
        this.avgPrice = avgPriceForTwo;
    }

    public Integer getNumberOfUsersRated() {
        return numberUsersRated;
    }

    public void setNumberOfUsersRated(Integer numberOfUsersRated) {
        this.numberUsersRated = numberOfUsersRated;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

   /* public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }*/

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
