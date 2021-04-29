//package com.yu.jangtari.CategoryTest;
//
//import com.yu.jangtari.RepositoryTest;
//import com.yu.jangtari.domain.Category;
//import com.yu.jangtari.repository.category.CategoryRepository;
//import com.yu.jangtari.repository.category.CategoryRepositoryQuerydsl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.LongStream;
//
//public class QuerydslTest extends RepositoryTest {
//
//    @Autowired
//    private CategoryRepositoryQuerydsl repository;
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @BeforeEach
//    void beforeEach() {
//
//    }
//
//    private List<Category> makeTestCategories() {
//        List<Category> categories = new ArrayList<>();
//        LongStream.range(0, 10).forEach(i -> {
//        });
//        return categories;
//    }
//}
