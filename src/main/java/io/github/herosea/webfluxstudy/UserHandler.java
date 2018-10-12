package io.github.herosea.webfluxstudy;

import io.github.herosea.webfluxstudy.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by nvwashi on 2018/10/10.
 */

@Component
@Slf4j
public class UserHandler {
    @Autowired
    UserService userService;

    private Mono<User> userMono;

    public Mono<ServerResponse> getUserList(ServerRequest request) { //Lambda 匿名参数
        Flux<User> userFlux = userService.findUserList();
        userFlux.subscribe(user -> log.info(user.toString()));
        return ServerResponse.ok().body(userFlux, User.class);
    }

    public Mono<ServerResponse> getUser(ServerRequest request) {
        String userId = request.pathVariable("userId");
        Mono<User> userMono = userService.findUserById(Long.parseLong(userId));
        userMono.subscribe(user -> log.info(user.toString()));
        log.info("return " + userId);
        return ServerResponse.ok().body(userMono, User.class);
    }
}
 

