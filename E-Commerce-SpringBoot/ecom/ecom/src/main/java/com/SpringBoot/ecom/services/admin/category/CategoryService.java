package com.SpringBoot.ecom.services.admin.category;

import com.SpringBoot.ecom.dto.CategoryDto;
import com.SpringBoot.ecom.entity.Category;
import com.SpringBoot.ecom.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryService implements  CategoryServiceImp {

    private final CategoryRepository categoryRepository;

    public Category createCategory(CategoryDto categoryDto) {

        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        return categoryRepository.save(category);
    }


}
