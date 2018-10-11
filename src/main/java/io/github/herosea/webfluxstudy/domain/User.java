package io.github.herosea.webfluxstudy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author nvwashi
 * @date 2018/10/10
 */
@Data
@AllArgsConstructor
public class User {
    private String name;
    private int age;
}
