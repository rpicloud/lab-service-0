package com.rpicloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class Application {

	private Boolean crash = false;
	private Integer sleep = 0;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@RequestMapping("/test")
	public ResponseEntity<List<String>> tests() throws InterruptedException {
		List<String> list = new ArrayList<>();
		list.add("H");
		list.add("e");
		list.add("l");
		list.add("l");
		list.add("o");
		if(crash){
			throw new NullPointerException();
		}
		else if(sleep > 0){
			Thread.sleep(sleep*1000);
		}
		return new ResponseEntity<List<String>>(list, HttpStatus.OK);
	}

	@RequestMapping("/crash")
	public String crash(){
		if(crash){
            crash=false;
			throw new NullPointerException();
		}
		crash=true;
		return "I'm gonna crash next time you call me!";
	}

	@RequestMapping(value= "/timeout/{seconds}")
	public String timeout(@PathVariable Integer seconds) throws InterruptedException {
		if(sleep > 0){
			Thread.sleep(sleep*1000);
            sleep=0;
		}
        sleep=seconds;
		return "I'm gonna sleep for " + seconds + " seconds next time you call me!";
	}


}
