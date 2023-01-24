package com.bobocode.beans.autowiring.nosuchbean;

import com.bobocode.beans.autowiring.success.CustomService;
import com.bobocode.beans.autowiring.success.MessageService;
import com.bobocode.context.annotation.Autowire;
import com.bobocode.context.annotation.Bean;

@Bean
public class PrinterService implements CustomService {

    @Autowire
    private MessageService messageService;

    public String print() {
        return messageService.getMessage();
    }

}
