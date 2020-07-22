package com.xz.interfaces;

import com.xz.annotations.ApiServer;
import com.xz.domain.City;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ApiServer("http:localhost:8080/")
public interface ICityApi {

    @GetMapping("/findAll")
    Flux<City> getAllCity();

    @PostMapping("/add")
    Flux<City> addCity(@RequestBody Mono<City> city);

}
