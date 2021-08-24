package cn.lizhi.serviceOss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) // 不需要加载数据库的自动配置类
@ComponentScan(basePackages = {"cn.lizhi"}) // spring在创建容器时要扫描的包
@EnableDiscoveryClient
@EnableFeignClients
public class OssApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(OssApplication.class, args);
//        String[] names = applicationContext.getBeanDefinitionNames();
//        for (String name : names) {
//            System.out.println(name);
//        }
    }
}
