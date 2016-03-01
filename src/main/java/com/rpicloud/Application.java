package com.rpicloud;

import com.rpicloud.interfaces.IService1;
import com.rpicloud.models.Resource1;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
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
	private String service1host;
	private String service1port;

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


	@RequestMapping(value = "/request1")
	public ResponseEntity<List<Resource1>> request1() {

		IService1 service1 = Feign.builder()
				.decoder(new JacksonDecoder())
				.target(IService1.class, "http://"+service1host+":"+service1port);

		// Fetch and print a list of the contributors to this library.
		List<Resource1> resources = service1.resources();

		return new ResponseEntity<List<Resource1>>(resources, HttpStatus.OK);
	}

	@Autowired
	void setEnvironment(Environment env){
		service1host = env.getProperty("configuration.service1.host");
		service1port = env.getProperty("configuration.service1.port");
	}
}
