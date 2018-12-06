package org.upgrad.services;

import org.springframework.stereotype.Service;
import org.upgrad.models.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category getCategory(String categoryName);
}
