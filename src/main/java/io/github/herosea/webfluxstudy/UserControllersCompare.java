package io.github.herosea.webfluxstudy;

import io.github.herosea.webfluxstudy.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author nvwashi
 * @date 2018/10/12
 */
@RestController
@RequestMapping("/compare/users")
@Slf4j
public class UserControllersCompare {

    private final UserService userService;

    public UserControllersCompare(UserService userService) {
        this.userService = userService;
    }

    public void setUser(Long uid, User user) {
        userService.setUser(uid, user);
    }

    @GetMapping("/{uid}")
    public Mono<User> findUserById(@PathVariable("uid") Long uid) {
        Mono<User> userMono = userService.findUserById(uid);
        userMono.subscribe(user -> log.info(user.toString()));
        return userMono;
    }

    @GetMapping()
    public Flux<User> findUserList() {
        return userService.findUserList();
    }
}
