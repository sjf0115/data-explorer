package com.data.explorer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.data.explorer.mapper")
public class DataExplorerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataExplorerApplication.class, args);
    }
}
