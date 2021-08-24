package cn.lizhi.serviceOrder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("cn.lizhi")
@EnableDiscoveryClient // 开启服务注册和服务发现
@EnableFeignClients // 对远程调用的httpClient的封装
@ComponentScan(basePackages = {"cn.lizhi"})
public class OrdersApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdersApplication.class, args);
    }
}
