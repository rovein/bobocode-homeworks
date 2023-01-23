package com.bobocode.context.impl;

import com.bobocode.context.ApplicationContext;
import com.bobocode.context.exception.NoSuchBeanException;
import com.bobocode.context.exception.NoUniqueBeanException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AnnotationConfigApplicationContextTest {

    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        applicationContext = new AnnotationConfigApplicationContext("com.bobocode.context");
    }

    @Test
    @Order(1)
    void getBeanByTypeReturnsCorrectBean() {
        MessageService messageService = applicationContext.getBean(MessageService.class);
        assertEquals("Hello", messageService.getMessage());
    }

    @Test
    @Order(2)
    void getBeanByTypeWhenNoBeanPresentInContext() {
        assertThrows(NoSuchBeanException.class, () -> applicationContext.getBean(List.class));
    }

    @Test
    @Order(3)
    void getBeanByTypeWhenNoUniqueBeanPresentInContext() {
        assertThrows(NoUniqueBeanException.class, () -> applicationContext.getBean(CustomService.class));
    }

    @Test
    @Order(4)
    void getBeanByByNameReturnsCorrectBean() {
        MessageService messageService = applicationContext.getBean("messageService", MessageService.class);
        assertEquals("Hello", messageService.getMessage());
    }

    @Test
    @Order(5)
    void getBeanByByNameAndSuperclassReturnsCorrectBean() {
        CustomService messageService = applicationContext.getBean("messageService", CustomService.class);
        assertEquals("Hello", ((MessageService) messageService).getMessage());
    }

    @Test
    @Order(6)
    void getBeanByNonPresentName() {
        assertThrows(NoSuchBeanException.class, () -> applicationContext.getBean("customService", CustomService.class));
    }

    @Test
    @Order(7)
    void getAllBeansReturnsCorrectMap() {
        Map<String, CustomService> customServices = applicationContext.getAllBeans(CustomService.class);

        assertEquals(2, customServices.size());

        assertTrue(customServices.containsKey("messageService"));
        assertTrue(customServices.containsKey("printerService"));

        assertTrue(customServices.values().stream()
                .anyMatch(bean -> bean.getClass().isAssignableFrom(MessageService.class)));
        assertTrue(customServices.values().stream()
                .anyMatch(bean -> bean.getClass().isAssignableFrom(PrinterService.class)));
    }
}
