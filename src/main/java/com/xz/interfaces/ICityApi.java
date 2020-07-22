package com.xz.interfaces;

import com.xz.annotation.ServerInfo;
import com.xz.domain.City;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

@ServerInfo("http:localhost:8080/")
public interface ICityApi {

    @GetMapping("/finAll")
    Flux<City> getAllCity();
}
