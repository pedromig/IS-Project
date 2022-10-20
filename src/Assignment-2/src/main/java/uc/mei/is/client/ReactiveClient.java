package uc.mei.is.client;

import java.util.Collections;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import uc.mei.is.entity.Teacher;


@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
public class ReactiveClient {
    
    @Bean
    WebClient client () {
        return WebClient.create("http://localhost:8080");
    }

    @Bean
    CommandLineRunner demo (WebClient client) {
        return args -> {

            client
                .get()
                .uri("/teachers")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Teacher.class))
                .subscribe(System.out::println);
        };
    }


    public static void main(String [] args) {
        new SpringApplicationBuilder(ReactiveClient.class)
            .properties(Collections.singletonMap("server.port", "8081"))
            .run(args);
    }

}
