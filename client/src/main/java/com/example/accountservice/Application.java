package com.example.accountservice;

import com.example.accountservice.component.client.ClientPackageMarker;
import com.example.accountservice.component.system.SystemPackageMarker;
import com.example.accountservice.component.system.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackageClasses = {ClientPackageMarker.class, SystemPackageMarker.class})
@Import(AppConfig.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
