package com.eason.wiki.controller;

import com.eason.wiki.req.CategoryQueryReq;
import com.eason.wiki.req.CategorySaveReq;
import com.eason.wiki.resp.CommonResp;
import com.eason.wiki.resp.CategoryQueryResp;
import com.eason.wiki.resp.PageResp;
import com.eason.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


@RestController
@RequestMapping("/category")
public class CategoryController {


    @Resource
    private CategoryService categoryService;

    @GetMapping("/list")
    public CommonResp list(CategoryQueryReq req){
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list =  categoryService.list(req);
        resp.setContent(list);
        return resp;
    }


    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq req){
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }
}
