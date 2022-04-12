package com.mjdc.pts;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

public class AppContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static Object getBean(final Class<?> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    public static Environment getEnv() {
        return applicationContext.getEnvironment();
    }

    @SuppressWarnings("checkstyle:DesignForExtension")
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        AppContext.applicationContext = applicationContext;
    }
}
