package com.example.demoapply;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author
 */
@SpringBootApplication
@ComponentScan(value = { "com.example.demoapply","com.example.demoapply.exceptionive"})
public class DemoApplyApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplyApplication.class, args);

    }

}
