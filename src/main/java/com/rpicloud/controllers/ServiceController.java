package com.rpicloud.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rpicloud.interfaces.IService1;
import com.rpicloud.models.Resource1;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mixmox on 08/03/16.
 */
@Component
@RestController
public class ServiceController {

    private String service1host;
    private String service1port;

    @HystrixCommand(fallbackMethod = "open")
    @RequestMapping("/circuit")
    public ResponseEntity<List<Resource1>> request1() {

        IService1 service1 = Feign.builder()
                .decoder(new JacksonDecoder())
                .target(IService1.class, "http://"+service1host+":"+service1port);

        // Fetch and print a list of the contributors to this library.
        List<Resource1> resources = service1.resources();

        return new ResponseEntity<List<Resource1>>(resources, HttpStatus.OK);
    }

    public ResponseEntity<List<Resource1>> open() {
        List<Resource1> list = new ArrayList<>();
        list.add(new Resource1("Circuit is open!"));

        return new ResponseEntity<List<Resource1>>(list, HttpStatus.OK);
    }

    @Autowired
    void setEnvironment(Environment env){
        service1host = env.getProperty("configuration.service1.host");
        service1port = env.getProperty("configuration.service1.port");
    }
}
