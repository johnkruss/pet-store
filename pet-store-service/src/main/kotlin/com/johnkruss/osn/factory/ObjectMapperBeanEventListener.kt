package com.johnkruss.osn.factory

import com.fasterxml.jackson.databind.ObjectMapper
import com.johnkruss.osn.ObjectMapperBuilder
import io.micronaut.context.event.BeanCreatedEvent
import io.micronaut.context.event.BeanCreatedEventListener
import io.micronaut.core.annotation.NonNull
import jakarta.inject.Singleton

@Singleton
class ObjectMapperBeanEventListener : BeanCreatedEventListener<ObjectMapper> {
    override fun onCreated(event: @NonNull BeanCreatedEvent<ObjectMapper>): ObjectMapper {
        return ObjectMapperBuilder().build()
    }
}
