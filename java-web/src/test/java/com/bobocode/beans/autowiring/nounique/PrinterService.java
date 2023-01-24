package com.bobocode.beans.autowiring.nounique;

import com.bobocode.context.annotation.Autowire;
import com.bobocode.context.annotation.Bean;

@Bean
public class PrinterService implements CustomService {

    @Autowire
    private CustomService customService;

}
