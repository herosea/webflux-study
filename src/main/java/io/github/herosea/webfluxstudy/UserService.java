package io.github.herosea.webfluxstudy;

import io.github.herosea.webfluxstudy.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.cache.CacheFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author nvwashi
 * @date 2018/10/10
 */
@Service
@Slf4j
public class UserService {

    private static final String CACHE_NAME = "user";
    private static final String KEY = "k";

    @Autowired
    private CacheManager cacheManager;

    private Map<Long, User> userMap = new ConcurrentHashMap<>();

    private Scheduler[] schedulers = new Scheduler[]{
            Schedulers.newSingle("user"),
            Schedulers.newSingle("user"),
            Schedulers.newSingle("user"),
            Schedulers.newSingle("user")
    };

    public void setUser(Long uid, User user) {
        userMap.put(uid, user);
    }

    public Mono<User> findUserById(Long uid) {
        Holder holder = cacheManager.getCache(CACHE_NAME).get(uid, () -> new Holder(uid));
        return Mono.just(holder).publishOn(schedulers[uid.hashCode() % schedulers.length])
                .map(h -> h.findUser());
    }

    public Flux<User> findUserList() {
//        return Flux.create(cityFluxSink -> {
//            getUserFlux().forEach(city -> {
//                cityFluxSink.next(city);
//            });
//            cityFluxSink.complete();
//        });

        return CacheFlux.lookup(reader, KEY)
                .onCacheMissResume(() -> {
                    return Flux.create(cityFluxSink -> {
                        getUserFlux().forEach(city -> {
                            cityFluxSink.next(city);
                        });
                        cityFluxSink.complete();
                    });
                })
                .andWriteWith(writer);
    }

    public class Holder {

        private Long uid;

        public Holder(Long uid) {
            log.info("new holder: " + uid);
            this.uid = uid;
        }

        private User user;

        public User findUser() {
            if (user == null) {
                this.user = getUserMono(uid);
            }
            return this.user;
        }
    }

    private List<User> getUserFlux() {
        try {
            Thread.sleep(100 + new Random().nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("get all");
        List<User> userList = new ArrayList<>();
        Set<Map.Entry<Long, User>> entries = userMap.entrySet();
        entries.stream().forEach(entry -> userList.add(entry.getValue()));
        userList.add(new User("随机" + System.currentTimeMillis(), new Random().nextInt(100000000)));
        return userList;
    }

    private User getUserMono(Long uid) {
        log.info("start get:" + uid);
        try {
            Thread.sleep(1000 + new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        User user = userMap.getOrDefault(uid, new User("随机" + System.currentTimeMillis(), new Random().nextInt(100000000)));
        log.info("finish get:" + uid);
        return user;
    }

//    private Function<Long, Mono<Signal<? extends Holder>>> monoReader = key -> Mono.<Signal<Holder>>
//            justOrEmpty(getFromCache(key));
////    private Function<> CacheMono.MonoCacheWriter<String, Task> = (key, value) -> Mono.fromRunnable(() -> manager.getCache("tasks").put(key, value));
//
//    private BiFunction<Long, Signal<? extends Holder>, Mono<Void>> monoWriter = (key, value) -> Mono.fromRunnable(() -> cacheManager.getCache(CACHE_NAME).put(key, value));
//
//    private Signal getFromCache(Object key) {
//        Cache.ValueWrapper wrapper = cacheManager.getCache(CACHE_NAME)
//                .get(key);
//        if (wrapper == null) {
//            return null;
//        }
//        return (Signal) wrapper.get();
//    }

    @SuppressWarnings("unchecked")
    private Function<String, Mono<List<Signal<User>>>> reader = k -> Mono
            .justOrEmpty((Optional.ofNullable((List<User>) (cacheManager.getCache(CACHE_NAME).get(k, List.class)))))
            .flatMap(v -> Flux.fromIterable(v).materialize().collectList());

    private BiFunction<String, List<Signal<User>>, Mono<Void>> writer = (k, sigs) -> Flux.fromIterable(sigs)
            .dematerialize().collectList().doOnNext(l -> cacheManager.getCache(CACHE_NAME).put(k, l)).then();

}
