package com.samajh.samajh_backend.controller;

import com.samajh.samajh_backend.service.HomeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final com.samajh.samajh_backend.service.HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/")
    public String home() {
        return homeService.getMessage();
    }
    
}
