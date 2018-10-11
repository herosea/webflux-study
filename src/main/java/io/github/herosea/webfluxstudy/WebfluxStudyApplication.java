package io.github.herosea.webfluxstudy;

import io.github.herosea.webfluxstudy.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableCaching
public class WebfluxStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxStudyApplication.class, args);
    }

    @Component
    static class Init implements CommandLineRunner {
        @Autowired
        UserService userService;

        @Override
        public void run(String... args) throws Exception {
            userService.setUser(1L, new User("carlos", 18));
            userService.setUser(2L, new User("lisa", 19));
            userService.setUser(3L, new User("mike", 17));
            userService.setUser(4L, new User("tom", 16));
            userService.setUser(5L, new User("amy", 15));
        }
    }
}
