package org.group.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@MapperScan("org.group.mall.dao")
@SpringBootApplication
//@EnableAsync
public class MallApplication {
  public static void main(String[] args) {
    System.out.println("http://localhost:28089");
    System.out.println("http://localhost:28089/admin/login");
    SpringApplication.run(MallApplication.class, args);
  }
}
