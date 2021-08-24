package cn.lizhi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class EduApplication {

    public static void main(String[] args) {


        ConfigurableApplicationContext run = SpringApplication.run(EduApplication.class, args); // 其返回值是spring容器context
//        String[] names = run.getBeanDefinitionNames(); // 获取容器中对象的bean name
//        for (String name : names) {
//            System.out.println(name);
//        }
    }
}
