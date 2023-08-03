package com.ethan.mall.controller;

import com.ethan.mall.common.ApiRestResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class AdminController {
    @GetMapping("/add")
    public ApiRestResponse cartList(){
        return null;
    }

}
