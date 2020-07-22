package com.xz.controller;

import com.xz.domain.City;
import com.xz.interfaces.ICityApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {

    @Autowired
    private ICityApi iCity;

    @GetMapping(value = "/findAll")
    public Flux<City> getAllCity() {
        iCity.getAllCity();
        City city = City.builder().cityName("wuhan").description("hot").provinceId(7701l).build();
        iCity.addCity(Mono.just(city));
        return null;
    }

}
