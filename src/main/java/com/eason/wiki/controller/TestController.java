package com.eason.wiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class TestController {

    @Value("${author:Test}")
    private String author;

    @GetMapping("/hello")
    public String hello(){
        return "Hello "+ author;
    }

    @PostMapping("/hello/post")
    public String helloPost(String name){
        return "Hello World Post " + name;
    }


}
