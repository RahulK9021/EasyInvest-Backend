package com.investkaro.controller;

import com.investkaro.entity.Industry;
import com.investkaro.service.IndustryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class IndustryController {
    private final IndustryService industryService;

    public IndustryController(IndustryService industryService) {
        this.industryService = industryService;
    }

    @PostMapping("/admin/industries")
    public Industry createIndustry(@RequestParam String name){
        return industryService.createIndustry(name);
    }

    @GetMapping("/api/industries")
    public List<Industry> getAllIndustry(){
        return industryService.getAllIndustries();
    }
}
