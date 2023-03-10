package com.cqcnt;

import com.cqcnt.util.ProjectUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class CqcntdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CqcntdemoApplication.class, args);
    }

}
