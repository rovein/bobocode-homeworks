package com.bobocode.context.impl;

import com.bobocode.beans.autowiring.success.CustomService;
import com.bobocode.beans.autowiring.success.MessageService;
import com.bobocode.beans.autowiring.success.PrinterService;
import com.bobocode.context.ApplicationContext;
import com.bobocode.context.exception.NoSuchBeanException;
import com.bobocode.context.exception.NoUniqueBeanException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayName("AnnotationConfigApplicationContext test")
public class AnnotationConfigApplicationContextTest {

    private ApplicationContext applicationContext;

    @Nested
    @Order(1)
    @DisplayName("1. Beans retrieving test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class BeansRetrievingTest {
        @BeforeEach
        void setUp() {
            applicationContext = new AnnotationConfigApplicationContext("com.bobocode.beans.autowiring.success");
        }

        @Test
        @Order(1)
        @DisplayName("Bean is correctly retrieved from the context by it`s type")
        void getBeanByTypeReturnsCorrectBean() {
            MessageService messageService = applicationContext.getBean(MessageService.class);
            assertEquals("Hello", messageService.getMessage());
        }

        @Test
        @Order(2)
        @DisplayName("NoSuchBeanException is thrown when there is no bean in context")
        void getBeanByTypeWhenNoBeanPresentInContext() {
            assertThrows(NoSuchBeanException.class, () -> applicationContext.getBean(List.class));
        }

        @Test
        @Order(3)
        @DisplayName("NoUniqueBeanException is thrown when there is no unique bean in context")
        void getBeanByTypeWhenNoUniqueBeanPresentInContext() {
            assertThrows(NoUniqueBeanException.class, () -> applicationContext.getBean(CustomService.class));
        }

        @Test
        @Order(4)
        @DisplayName("Bean is correctly retrieved from the context by it`s name")
        void getBeanByByNameReturnsCorrectBean() {
            MessageService messageService = applicationContext.getBean("messageService", MessageService.class);
            assertEquals("Hello", messageService.getMessage());
        }

        @Test
        @Order(5)
        @DisplayName("Bean is correctly retrieved from the context by it`s explicitly provided name")
        void getBeanByByExplicitNameReturnsCorrectBean() {
            PrinterService printerService = applicationContext.getBean("printer-bean", PrinterService.class);
            assertNotNull(printerService);
        }

        @Test
        @Order(6)
        @DisplayName("Bean is correctly retrieved by it`s name and superclass")
        void getBeanByByNameAndSuperclassReturnsCorrectBean() {
            CustomService messageService = applicationContext.getBean("messageService", CustomService.class);
            assertEquals("Hello", ((MessageService) messageService).getMessage());
        }

        @Test
        @Order(7)
        @DisplayName("NoSuchBeanException is thrown when retrieving bean by non-existing name")
        void getBeanByNonPresentName() {
            assertThrows(NoSuchBeanException.class, () -> applicationContext.getBean("customService", CustomService.class));
        }

        @Test
        @Order(8)
        @DisplayName("Get all beans returns correct map of objects")
        void getAllBeansReturnsCorrectMap() {
            Map<String, CustomService> customServices = applicationContext.getAllBeans(CustomService.class);

            assertEquals(2, customServices.size());

            assertTrue(customServices.containsKey("messageService"));
            assertTrue(customServices.containsKey("printer-bean"));

            assertTrue(customServices.values().stream()
                    .anyMatch(bean -> bean.getClass().isAssignableFrom(MessageService.class)));
            assertTrue(customServices.values().stream()
                    .anyMatch(bean -> bean.getClass().isAssignableFrom(PrinterService.class)));
        }
    }

    @Nested
    @Order(2)
    @DisplayName("2. Autowiring post processing test")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class AutowiringPostProcessingTest {
        @Test
        @Order(1)
        @DisplayName("Field is successfully autowired")
        void autowiringFieldIsSetCorrectly() {
            applicationContext = new AnnotationConfigApplicationContext("com.bobocode.beans.autowiring.success");
            PrinterService printerService = applicationContext.getBean(PrinterService.class);
            MessageService messageService = applicationContext.getBean(MessageService.class);
            assertEquals(messageService.getMessage(), printerService.print());
        }

        @Test
        @Order(2)
        @DisplayName("NoSuchBeanException is thrown if there no bean that can be autowired")
        void autowiringFieldWhenNoSuchBeanPresentInContext() {
            assertThrows(NoSuchBeanException.class,
                    () -> new AnnotationConfigApplicationContext("com.bobocode.beans.autowiring.nosuchbean"));
        }

        @Test
        @Order(3)
        @DisplayName("NoUniqueBeanException is thrown if there are several candidates for autowiring")
        void autowiringFieldWhenNoUniqueBeanPresentInContext() {
            assertThrows(NoUniqueBeanException.class,
                    () -> new AnnotationConfigApplicationContext("com.bobocode.beans.autowiring.nounique"));
        }
    }
}
