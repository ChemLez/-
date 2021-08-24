package cn.lizhi.serviceAcl;

import cn.lizhi.serviceAcl.entity.AclPermission;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = {"cn.lizhi.serviceAcl.mapper"})
@ComponentScan(basePackages = {"cn.lizhi"})
@EnableDiscoveryClient
public class AclApplication {

    public static void main(String[] args) {
        SpringApplication.run(AclApplication.class, args);
    }
}
