package cn.lizhi.serviceUcenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("cn.lizhi.serviceUcenter.mapper")
@ComponentScan(basePackages = {"cn.lizhi"})
public class ServiceUcApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceUcApplication.class, args);
    }
}
