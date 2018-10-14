package io.github.herosea.webfluxstudy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

/**
 * Created by nvwashi on 2018/10/10.
 */
@Configuration
public class UserRoutes {

    @Bean
    @Autowired
    public RouterFunction<ServerResponse> routersFunction(UserHandler userHandler) {
        return RouterFunctions
//                .route(POST("/api/users").and(accept(MediaType.APPLICATION_JSON)), userHandler::getUserList);
                .route(RequestPredicates.POST("/api/users/{userId}").and(accept(MediaType.APPLICATION_JSON)), userHandler::getUser);
    }
}
 

