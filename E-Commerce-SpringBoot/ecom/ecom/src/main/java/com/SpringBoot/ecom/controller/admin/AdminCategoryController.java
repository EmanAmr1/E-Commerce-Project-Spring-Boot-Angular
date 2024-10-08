package com.SpringBoot.ecom.controller.admin;

import com.SpringBoot.ecom.dto.CategoryDto;
import com.SpringBoot.ecom.entity.Category;
import com.SpringBoot.ecom.services.admin.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDto categoryDto) {
     Category category = categoryService.createCategory(categoryDto);
     return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }
}
