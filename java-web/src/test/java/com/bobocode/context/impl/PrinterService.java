package com.bobocode.context.impl;

import com.bobocode.context.annotation.Bean;

@Bean
public class PrinterService implements CustomService {

    public void print() {
        System.out.println("Hello");
    }

}
