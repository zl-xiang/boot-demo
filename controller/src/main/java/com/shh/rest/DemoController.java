package com.shh.rest;


import com.shh.service.IDemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("demo")
public class DemoController {

    private final IDemoService demoService ;

    public DemoController(IDemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("test")
    public String test() {

        return demoService.hello("arseBurger!");
    }
}
