package com.chewy.osn.factory;

import com.chewy.osn.ObjectMapperBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import jakarta.inject.Singleton;

@Singleton
public class ObjectMapperBeanEventListener implements BeanCreatedEventListener<ObjectMapper> {

    @Override
    public ObjectMapper onCreated(BeanCreatedEvent<ObjectMapper> event) {
        if (event.getBeanIdentifier().getName().equals("yaml")) {
            return event.getBean();
        }
        return new ObjectMapperBuilder().build();
    }
}