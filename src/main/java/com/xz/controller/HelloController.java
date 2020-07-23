package com.xz.controller;

import com.xz.domain.City;
import com.xz.interfaces.ICityApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {

    @Autowired
    private ICityApi iCity;

    @GetMapping(value = "/findAll")
    public Flux<City> getAllCity() {
        Flux<City> allCity = iCity.getAllCity();
        //allCity.subscribe(System.out::println);
        return allCity;
    }

    @GetMapping(value = "/getCityById/{id}")
    public Mono<City> getCityById(@PathVariable("id") String id) {
        Mono<City> cityById = iCity.getCityById(id);
        //cityById.subscribe(System.out::println);
        return cityById;
    }

    @PostMapping(value = "/add")
    public Flux<City> add(@RequestBody City city) {
        Flux<City> cityFlux = iCity.addCity(Mono.just(city));
/*        cityFlux.subscribe(System.out::println);*/
        return cityFlux;
    }

}
