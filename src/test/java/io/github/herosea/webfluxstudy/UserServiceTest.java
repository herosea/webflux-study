package io.github.herosea.webfluxstudy;

import lombok.Data;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by nvwashi on 2018/10/10.
 */
public class UserServiceTest {

    String uri = "http://bullseye-webapi.volibear.k2.test.wacai.info/bse/putin/channel/dict";

    @Test
    public void findUserList() throws Exception {
        Mono<List<Dict>> resp = WebClient.create()
                .method(HttpMethod.GET)
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<WebResponse<List<Dict>>>(){}).map(
                        r -> r.getData()
                );
        System.out.println(resp.block());
    }

    @Data
    public static class WebResponse<T> {
        int code;
        String error;
        T data;
        Boolean needRetry;
    }

    @Data
    public static class Dict{
        private String key;
        private String value;
    }
}