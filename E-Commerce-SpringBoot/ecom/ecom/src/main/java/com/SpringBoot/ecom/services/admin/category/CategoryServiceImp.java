package com.SpringBoot.ecom.services.admin.category;


import com.SpringBoot.ecom.dto.CategoryDto;
import com.SpringBoot.ecom.entity.Category;

public interface CategoryServiceImp {

    Category createCategory(CategoryDto categoryDto);

}
