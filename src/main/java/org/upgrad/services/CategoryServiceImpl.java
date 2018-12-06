package org.upgrad.services;

import org.springframework.stereotype.Service;
import org.upgrad.models.Category;
import org.upgrad.repositories.CategoryRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }
    @Override
    public List<Category> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    @Override
    public Category getCategory(String categoryName){
        Optional<Category> categoryOptional = categoryRepository.getCategoryByName(categoryName);
        if(categoryOptional.isPresent()){
            return categoryOptional.get();
        }else{
            return null;
        }
    }

}
