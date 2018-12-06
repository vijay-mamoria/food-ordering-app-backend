package org.upgrad.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upgrad.models.Category;
import org.upgrad.services.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * This end point is used to get category details by name
     * @param categoryName
     * @return category details
     */
    @GetMapping("/{categoryName}")
    @CrossOrigin
    public ResponseEntity<?> getCategoryByName(@PathVariable String categoryName){
        Category category = categoryService.getCategory(categoryName);
        if(category == null){
            return new ResponseEntity<>("No Category by this name!",HttpStatus.BAD_REQUEST);

        }else{
            return new ResponseEntity<>(category,HttpStatus.OK);
        }
    }

    /**
     * This end point is used to get all the categories
     * @return all category details
     */
    @GetMapping("")
    @CrossOrigin
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
