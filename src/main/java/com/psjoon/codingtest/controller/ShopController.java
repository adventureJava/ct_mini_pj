package com.psjoon.codingtest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("shop")
public class ShopController {

    @Value("${iamport.api.key}")
    private String apiKey;

    @GetMapping("/api-key")
    public String getApiKey() {
        return apiKey;
    }

    @GetMapping("/product")
    public String product(Model model) {
        model.addAttribute("apiKey", apiKey);
        return "/shop/product";
    }
}
