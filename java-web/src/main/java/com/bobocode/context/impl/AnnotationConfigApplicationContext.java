package com.bobocode.context.impl;

import com.bobocode.context.ApplicationContext;
import com.bobocode.context.annotation.Bean;
import com.bobocode.context.exception.NoSuchBeanException;
import com.bobocode.context.exception.NoUniqueBeanException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toMap;

public class AnnotationConfigApplicationContext implements ApplicationContext {

    private final Map<String, Object> beans = new ConcurrentHashMap<>();

    public AnnotationConfigApplicationContext(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> beanClasses = reflections.getTypesAnnotatedWith(Bean.class);
        init(beanClasses);
    }

    @SneakyThrows
    private void init(Set<Class<?>> classes) {
        for (Class<?> type : classes) {
            String name = resolveBeanName(type);
            Object instance = type.getConstructor().newInstance();
            beans.put(name, instance);
        }
    }

    private String resolveBeanName(Class<?> type) {
        String explicitName = type.getAnnotation(Bean.class).value();
        return StringUtils.isBlank(explicitName) ? resolveTypeId(type) : explicitName;
    }

    private String resolveTypeId(Class<?> type) {
        String className = type.getSimpleName();
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    /**
     * Returns the bean instance that uniquely matches the given object type, if any.
     *
     * @param beanType type the bean must match; can be an interface or superclass
     * @return an instance of the single bean matching the required type
     * @throws NoSuchBeanException   if no bean of the given type was found
     * @throws NoUniqueBeanException if more than one bean of the given type was found
     */
    @Override
    public <T> T getBean(Class<T> beanType) {
        Map<String, T> matchingBeans = getAllBeans(beanType);
        if (matchingBeans.size() > 1) {
            throw new NoUniqueBeanException();
        }
        return matchingBeans.values().stream()
                .findAny()
                .map(beanType::cast)
                .orElseThrow(NoSuchBeanException::new);
    }

    /**
     * Returns the bean instance by it`s unique name within the context.
     *
     * @param name     bean ID by which it`s stored in the context
     * @param beanType type the bean will be casted to; can be an interface or superclass
     * @return an instance of the bean matching it`s name
     * @throws NoSuchBeanException if no bean of the given name was found
     */
    @Override
    public <T> T getBean(String name, Class<T> beanType) {
        return beans.entrySet().stream()
                .filter(beanEntry -> name.equals(beanEntry.getKey()))
                .findAny()
                .map(Map.Entry::getValue)
                .map(beanType::cast)
                .orElseThrow(NoSuchBeanException::new);
    }

    /**
     * Returns the map of all bean instances that match provided bean type.
     *
     * @param beanType type the beans must match; can be an interface or superclass
     * @return a map where key is a bean ID and value is a bean itself
     */
    @Override
    public <T> Map<String, T> getAllBeans(Class<T> beanType) {
        return beans.entrySet()
                .stream()
                .filter(entry -> beanType.isAssignableFrom(entry.getValue().getClass()))
                .collect(toMap(Map.Entry::getKey, entry -> beanType.cast(entry.getValue())));
    }

}
