package io.github.herosea.webfluxstudy;

import com.wacai.stanlee.datacentre.object.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 *
 * @author nvwashi
 * @date 2018/10/11
 */
@Component
@Slf4j
public class PhoenixHandler {

//    @Autowired
//    PhoenixService phoenixService;
//
//    public Mono<ServerResponse> list(ServerRequest request) {
//        Mono<PhoniexParam> param = request.bodyToMono(PhoniexParam.class);
//        Mono<ResultObject> mono = phoenixService.list()
//    }
}
