package io.github.herosea.webfluxstudy;

import io.github.herosea.webfluxstudy.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.cache.CacheFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author nvwashi
 * @date 2018/10/10
 */
@Service
public class UserService {

    private static final String CACHE_NAME = "user";
    private static final String KEY = "k";

    @Autowired
    private CacheManager cacheManager;

    private WebClient client = WebClient.create();

    private Map<Long, User> userMap = new ConcurrentHashMap<>();

    public void setUser(Long uid, User user) {
        userMap.put(uid, user);
    }

    public Mono<User> findUserById(Long uid) {
        return getUserMono(uid);
    }

    public Flux<User> findUserList() {
        return CacheFlux.lookup(reader, KEY)
                .onCacheMissResume(() -> getUserFlux())
                .andWriteWith(writer);
    }

    private Flux<User> getUserFlux() {
        List<User> userList = new ArrayList<>();
        Set<Map.Entry<Long, User>> entries = userMap.entrySet();
        entries.stream().forEach(entry -> userList.add(entry.getValue()));
        userList.add(new User("随机" + System.currentTimeMillis(), new Random().nextInt(100000000)));
        return Flux.fromIterable(userList);
    }

    private Mono<User> getUserMono(Long uid) {
        User user = userMap.getOrDefault(uid, new User("nick", 18));
        return Mono.just(user);
    }

    @SuppressWarnings("unchecked")
    private Function<String, Mono<List<Signal<User>>>> reader = k -> Mono
            .justOrEmpty((Optional.ofNullable((List<User>) (cacheManager.getCache(CACHE_NAME).get(k, List.class)))))
            .flatMap(v -> Flux.fromIterable(v).materialize().collectList());

    private BiFunction<String, List<Signal<User>>, Mono<Void>> writer = (k, sigs) -> Flux.fromIterable(sigs)
            .dematerialize().collectList().doOnNext(l -> cacheManager.getCache(CACHE_NAME).put(k, l)).then();

}
