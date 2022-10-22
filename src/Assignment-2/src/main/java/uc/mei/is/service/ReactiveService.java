package uc.mei.is.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.context.annotation.Bean;
//////////////////////////////////////////////////////////////////////////
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;


/*import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import uc.mei.is.entity.Teacher;






import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.stream.Stream;*/

@SpringBootApplication
@EnableR2dbcAuditing
//@RestController
public class ReactiveService {

    /*@GetMapping ("/teachers/{id}")
    Mono<Teacher> getAllTeachers(@PathVariable int id) {
        return Mono.just(Teacher.builder().id(id)
                                .name("João")
                                .build());
    }

    @GetMapping (produces = MediaType.TEXT_EVENT_STREAM_VALUE, value = "/teachers")
    Flux<Teacher> teachers () {

        Flux<Teacher> teacherFlux = Flux.fromStream( Stream.generate(()->Teacher.builder()
                        .id((int) System.currentTimeMillis())
                        .name("Miguel")
                        .build()));
        Flux<Long> durationFlux = Flux.interval(Duration.ofSeconds(1));

        return Flux.zip( teacherFlux, durationFlux).map(Tuple2::getT1);
    }*/

    public static void main(String[] args) {
        SpringApplication.run(ReactiveService.class, args);
    }

    /*@Bean
    ApplicationRunner initialize(DatabaseClient databaseClient) {
        //log.info("start data initialization...");
        return args -> {
            databaseClient
                    .sql("INSERT INTO  posts (title, content, metadata) VALUES (:title, :content, :metadata)")
                    .filter((statement, executeFunction) -> statement.returnGeneratedValues("id").execute())
                    .bind("title", "my first post")
                    .bind("content", "content of my first post")
                    .bind("metadata", Json.of("{\"tags\":[\"spring\", \"r2dbc\"]}"))
                    .fetch()
                    .first()
                    .subscribe(
                            data -> log.info("inserted data : {}", data),
                            error -> log.error("error: {}", error)
                    );

        };

    }*/

    // not working
    @Bean
    ConnectionFactoryInitializer initializer(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        /*ResourceDatabasePopulator resource =
            new ResourceDatabasePopulator(new ClassPathResource("schema.sql"));
        initializer.setDatabasePopulator(resource);*/
        return initializer;
    }

    /*@Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        //populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data.sql")));
        initializer.setDatabasePopulator(populator);

        return initializer;
    }*/
    
}


