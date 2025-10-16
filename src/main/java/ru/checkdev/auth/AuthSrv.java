package ru.checkdev.auth;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@OpenAPIDefinition(info = @Info(title = "Auth - authentication service", description = "Description"))
@SpringBootApplication
@EnableEurekaClient
public class AuthSrv {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AuthSrv.class);
        application.addListeners(new ApplicationPidFileWriter("./auth.pid"));
        application.run();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
