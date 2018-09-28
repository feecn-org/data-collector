package com.feecn.org;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author pczhangyu
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.feecn.org")
public class CollectApplication {

	public static void main(String[] args){
		SpringApplication.run(CollectApplication.class, args);
	}

}
