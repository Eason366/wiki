package com.eason.wiki.controller;

import com.eason.wiki.domain.Test;
import com.eason.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
public class TestController {

    @Value("${author:Test}")
    private String author;

    @Resource
    private TestService testService;

    @GetMapping("/hello")
    public String hello(){
        return "Hello "+ author;
    }

    @PostMapping("/hello/post")
    public String helloPost(String name){
        return "Hello World Post " + name;
    }

    @GetMapping("/test/list")
    public List<Test> list(){
        return testService.list();
    }

}
