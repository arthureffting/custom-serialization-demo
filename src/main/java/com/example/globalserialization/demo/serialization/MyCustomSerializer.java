package com.example.globalserialization.demo.serialization;

import com.example.globalserialization.demo.annotations.SerializerAdvice;
import com.example.globalserialization.demo.annotations.Identified;
import com.example.globalserialization.demo.annotations.Named;
import com.example.globalserialization.demo.annotations.ServiceDependent;
import com.example.globalserialization.demo.domain.IdentifiedResource;
import com.example.globalserialization.demo.domain.NamedResource;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MyCustomSerializer extends StdSerializer {

    @Autowired
    private ApplicationContext context;

    private Map<Class, Method> serializationMap = new HashMap<>();

    public MyCustomSerializer() {
        super(Object.class);

        Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(SerializerAdvice.class))
                .forEach(method -> {
                    SerializerAdvice processorMethod = method.getAnnotation(SerializerAdvice.class);
                    Class annotationClass = processorMethod.value();
                    serializationMap.put(annotationClass, method);
                });
    }

    @Override
    public void serialize(Object target, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        Class targetClass = target.getClass();


        for (Field field : targetClass.getDeclaredFields()) {

            //If the field has a custom annotation and a processor is found, serialize it using the processor
            Annotation[] annotations = field.getAnnotations();

            Optional<Method> serializationMethod = Arrays.stream(annotations) // Stream all field annotations
                    .filter(annotation -> serializationMap.containsKey(annotation.annotationType())) // Filters out annotations for which we don't have methods
                    .map(annotation -> serializationMap.get(annotation.annotationType())) // Gets the methods
                    .findFirst(); // Gets first available, you shouldn't have more than one

            //If a processor was found, serialize using it
            if (serializationMethod.isPresent()) {
                try {
                    serializationMethod.get().invoke(this, target, field, jsonGenerator);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else { //fallback to default serialization
                try {
                    PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(target.getClass(), field.getName());
                    jsonGenerator.writeObjectField(field.getName(), descriptor.getReadMethod().invoke(target));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        jsonGenerator.writeEndObject();
    }


    @SerializerAdvice(Named.class)
    public void nameField(Object target, Field field, JsonGenerator jsonGenerator) throws Exception {
        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(target.getClass(), field.getName());
        Object fieldValue = descriptor.getReadMethod().invoke(target);
        String name = ((NamedResource) fieldValue).getName();
        jsonGenerator.writeObjectField(field.getName(), name);
    }

    @SerializerAdvice(ServiceDependent.class)
    public void getFromService(Object target, Field field, JsonGenerator jsonGenerator) throws Exception {
        ServiceDependent annotation = field.getAnnotation(ServiceDependent.class);
        Class serviceBeanClass = annotation.service();
        String methodName = annotation.method();
        Object service = context.getBean(serviceBeanClass);
        Method method = service.getClass().getMethod(methodName, target.getClass());
        Object serviceMethodInvokeResult = method.invoke(service, target);
        jsonGenerator.writeObjectField(field.getName(), serviceMethodInvokeResult);

    }

    @SerializerAdvice(Identified.class)
    public void identifyField(Object target, Field field, JsonGenerator jsonGenerator) throws Exception {
        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(target.getClass(), field.getName());
        Object fieldValue = descriptor.getReadMethod().invoke(target);
        String name = ((IdentifiedResource) fieldValue).getId();
        jsonGenerator.writeObjectField(field.getName(), name);
    }

}
