package com.xz.controller;

import com.xz.domain.City;
import com.xz.interfaces.ICityApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class HelloController {

    @Autowired
    private ICityApi iCity;

    @GetMapping(value = "/findAll")
    public Flux<City> getAllCity() {
        log.info("getAll start");
        Flux<City> allCity = iCity.getAllCity();
        log.info("getAll end");
        //allCity.subscribe(System.out::println);
        return allCity;
    }

    /**
     * 和上面的做对比
     *
     * @return
     */
    @GetMapping(value = "/findAllMvc")
    public List<City> findAllMvc() {
        log.info("findAllMvc start");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        City no_where = City.builder().cityName("no where").build();
        log.info("findAllMvc end");
        //allCity.subscribe(System.out::println);
        return Arrays.asList(no_where);
    }


    @GetMapping(value = "/getCityById/{id}")
    public Mono<City> getCityById(@PathVariable("id") String id) {
        Mono<City> cityById = iCity.getCityById(id);
        /**
         * TODO 如果订阅,会调用两次接口,为什么?webFlux中是不需要手动订阅的
         */
        cityById.subscribe(System.out::println);
        return cityById;
    }

    @PostMapping(value = "/add")
    public Flux<City> add(@RequestBody City city) {
        Flux<City> cityFlux = iCity.addCity(Mono.just(city));
        /*        cityFlux.subscribe(System.out::println);*/
        return cityFlux;
    }

}
