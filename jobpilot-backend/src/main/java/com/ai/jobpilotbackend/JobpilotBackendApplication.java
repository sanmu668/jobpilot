package com.ai.jobpilotbackend;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ai.jobpilotbackend.*.mapper")
public class JobpilotBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobpilotBackendApplication.class, args);
    }

}
