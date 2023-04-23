package com.bobocode.beans.autowiring.success;

import com.bobocode.context.annotation.Bean;

@Bean
public class MessageService implements CustomService {

    public String getMessage() {
        return "Hello";
    }

}
