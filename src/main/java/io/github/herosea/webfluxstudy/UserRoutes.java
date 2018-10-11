package io.github.herosea.webfluxstudy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Created by nvwashi on 2018/10/10.
 */
@Configuration
public class UserRoutes {

    @Bean
    @Autowired
    public RouterFunction<ServerResponse> routersFunction(UserHandler userHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/api/users"), userHandler::getUserList)
                .and(RouterFunctions.route(RequestPredicates.GET("/api/users/{userId}"), userHandler::getUser));
    }
}
 

