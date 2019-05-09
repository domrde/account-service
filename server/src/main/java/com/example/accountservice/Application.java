package com.example.accountservice;

import com.example.accountservice.component.account.AccountPackageMarker;
import com.example.accountservice.component.metric.MetricPackageMarker;
import com.example.accountservice.system.SystemPackageMarker;
import com.example.accountservice.system.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackageClasses = {
		AccountPackageMarker.class,
		MetricPackageMarker.class,
		SystemPackageMarker.class
})
@Import(AppConfig.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
