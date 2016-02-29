package com.rpicloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	private Boolean crash = false;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@RequestMapping("/crash")
	public String crash(){
		if(crash){
			throw new NullPointerException();
		}
		crash=true;
		return "I'm gonna crash next time you call me!";
	}


}
