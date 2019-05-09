package com.example.accountservice;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SpringBootTest
@TestPropertySource(properties = {
        "autostart.load=false"
})
@AutoConfigureWireMock(port = 0)
public @interface WiremockTest {

    @AliasFor(annotation = AutoConfigureWireMock.class, attribute = "stubs")
    String stubs();
}
