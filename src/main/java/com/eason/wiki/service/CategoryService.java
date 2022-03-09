package com.eason.wiki.service;


import com.eason.wiki.domain.Category;
import com.eason.wiki.domain.CategoryExample;
import com.eason.wiki.mapper.CategoryMapper;
import com.eason.wiki.req.CategoryQueryReq;
import com.eason.wiki.req.CategorySaveReq;
import com.eason.wiki.resp.CategoryQueryResp;
import com.eason.wiki.resp.PageResp;
import com.eason.wiki.util.CopyUtil;
import com.eason.wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);


    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req){
        CategoryExample categoryExample = new CategoryExample();
        PageHelper.startPage(req.getPage(),req.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("Total length: {}",pageInfo.getTotal());
        LOG.info("Total pages: {}",pageInfo.getPages());


        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return  pageResp;
    }


    public void save(CategorySaveReq req){
        Category category = CopyUtil.copy(req,Category.class);
        if (ObjectUtils.isEmpty(req.getId())){
            //insert
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }else {
            // update
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
