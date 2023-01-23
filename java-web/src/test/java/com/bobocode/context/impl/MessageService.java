package com.bobocode.context.impl;

import com.bobocode.context.annotation.Bean;

@Bean
public class MessageService implements CustomService {

    public String getMessage() {
        return "Hello";
    }

}
