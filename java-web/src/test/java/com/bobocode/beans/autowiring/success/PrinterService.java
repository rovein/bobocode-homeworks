package com.bobocode.beans.autowiring.success;

import com.bobocode.context.annotation.Autowire;
import com.bobocode.context.annotation.Bean;

@Bean("printer-bean")
public class PrinterService implements CustomService {

    @Autowire
    private MessageService messageService;

    public String print() {
        return messageService.getMessage();
    }

}
