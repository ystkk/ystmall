package com.ystmall.service;

import com.ystmall.common.ServerResponse;
import com.ystmall.pojo.Category;

import java.util.List;

public interface ICategoryService {

    ServerResponse addCatagory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
