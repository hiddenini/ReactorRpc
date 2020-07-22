package com.xz.controller;

import com.xz.domain.City;
import com.xz.interfaces.ICityApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class HelloController {

    @Autowired
    private ICityApi iCity;

    @GetMapping(value = "/findAll")
    public Flux<City> getAllCity() {
        return iCity.getAllCity();
    }
}
