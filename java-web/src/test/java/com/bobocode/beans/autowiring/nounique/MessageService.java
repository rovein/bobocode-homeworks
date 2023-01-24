package com.bobocode.beans.autowiring.nounique;

import com.bobocode.context.annotation.Bean;

@Bean
public class MessageService implements CustomService {

    public String getMessage() {
        return "Hello";
    }

}
