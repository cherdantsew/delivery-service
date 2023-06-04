package com.deliverengine.deliver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.deliverengine.*")
public class DeliverServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliverServiceApplication.class, args);
    }

}
