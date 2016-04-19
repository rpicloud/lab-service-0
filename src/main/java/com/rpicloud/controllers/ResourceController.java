package com.rpicloud.controllers;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rpicloud.interfaces.IService1;
import com.rpicloud.models.Resource;
import com.rpicloud.models.ServerState;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mixmox on 08/03/16.
 */
@Component
@RestController
public class ResourceController {

    private String service1host;
    private String service1port;
    private ServerState state = new ServerState();

    @Autowired
    void setEnvironment(Environment env){
        service1host = env.getProperty("configuration.service1.host");
        service1port = env.getProperty("configuration.service1.port");
    }

    @HystrixCommand(fallbackMethod = "open", commandKey = "resources")
    @RequestMapping("/resources")
    public ResponseEntity<List<Resource>> request1() {

        IService1 service1 = Feign.builder()
                .decoder(new JacksonDecoder())
                .target(IService1.class, "http://"+service1host+":"+service1port);

        // Fetch and print a list of the contributors to this library.
        List<Resource> resources = service1.resources();

        return new ResponseEntity<List<Resource>>(resources, HttpStatus.OK);
    }

    public ResponseEntity<List<Resource>> open() {
        List<Resource> list = new ArrayList<>();
        list.add(new Resource("Circuit is open!"));

        return new ResponseEntity<List<Resource>>(list, HttpStatus.PARTIAL_CONTENT);
    }

    // Enable/disable circuit breaker
    @RequestMapping("/circuitbreaker/enabled/{enabled}")
    public ResponseEntity<String> circuitbreaker(@PathVariable boolean enabled) {
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.resources.circuitBreaker.enabled", enabled);
        return new ResponseEntity<String>(enabled?"Enabled":"Disabled", HttpStatus.OK);
    }

    // Set timeout on circuit breaker
    @RequestMapping("/circuitbreaker/timeout/{duration}")
    public ResponseEntity<String> setTimeout(@PathVariable int duration){
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.resources.execution.isolation.thread.timeoutInMilliseconds", duration);
        return new ResponseEntity<String>("Duration in msec: " + duration, HttpStatus.OK);
    }

    // Enable/disable fallback
    @RequestMapping("/fallbackenabled/{enabled}")
    public ResponseEntity<String> fallback(@PathVariable boolean enabled) {
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.resources.fallback.enabled", enabled);
        return new ResponseEntity<String>(enabled?"Enabled":"Disabled", HttpStatus.OK);
    }


    @RequestMapping("/resultset/{amount}")
    public String resultSet(@PathVariable int amount) {
        state.setAmount(amount);
        return "Amount: " + state.getAmount();
    }

}
