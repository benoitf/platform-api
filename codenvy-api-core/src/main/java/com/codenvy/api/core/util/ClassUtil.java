/*
 * CODENVY CONFIDENTIAL
 * __________________
 * 
 *  [2012] - [2014] Codenvy, S.A. 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.api.core.util;

import com.codenvy.api.core.ApiException;
import com.codenvy.api.core.rest.shared.ParameterType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;

/**
 * @author Eugene Voevodin
 */
public final class ClassUtil {

    private ClassUtil() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static ParameterType getParameterType(Class<?> clazz) {
        if (clazz == String.class) {
            return ParameterType.String;
        }
        // restriction for collections which allowed for QueryParam annotation
        if (clazz == List.class || clazz == Set.class || clazz == SortedSet.class) {
            return ParameterType.Array;
        }
        if (clazz == Boolean.class || clazz == boolean.class) {
            return ParameterType.Boolean;
        }
        if (clazz == short.class || clazz == int.class || clazz == long.class || clazz == float.class || clazz == double.class ||
            clazz == Short.class || clazz == Integer.class || clazz == Long.class || clazz == Float.class || clazz == Double.class) {
            return ParameterType.Number;
        }
        return ParameterType.Object;
    }

    public static <T extends Annotation> T getAnnotationIfPresent(Annotation[] sourceAnnotation, Class<T> target) {
        for (Annotation source : sourceAnnotation) {
            if (source.annotationType().equals(target)) {
                return (T)source;
            }
        }
        return null;
    }

    public static <T extends Annotation> T getClassAnnotation(Class<?> clazz, Class<T> annotationClass) {
        T annotation = clazz.getAnnotation(annotationClass);
        if (annotation == null) {
            for (Class<?> c = clazz.getSuperclass(); annotation == null && c != null && c != Objects.class; c = c.getSuperclass()) {
                annotation = c.getAnnotation(annotationClass);
            }
        }
        return annotation;
    }

    public static <T extends Annotation> T getMethodAnnotation(Method method, Class<T> annotationClass) {
        T annotation = method.getAnnotation(annotationClass);
        if (annotation == null) {
            for (Class<?> c = method.getDeclaringClass().getSuperclass();
                 annotation == null && c != null && c != Object.class;
                 c = c.getSuperclass()) {
                Method inherited = null;
                try {
                    inherited = c.getMethod(method.getName(), method.getParameterTypes());
                } catch (NoSuchMethodException ignored) {
                }
                if (inherited != null) {
                    annotation = inherited.getAnnotation(annotationClass);
                }
            }
        }
        return annotation;
    }

    public static List<Field> getFields(Class<?> clazz, AnnotationsFilter filter) throws ApiException {
        Field[] allFields = clazz.getFields();
        List<Field> filteredFields = new LinkedList<>();
        for (Field field : allFields) {
            if (filter.accept(field.getAnnotations())) {
                filteredFields.add(field);
            }
        }
        return filteredFields;
    }

    public static interface AnnotationsFilter {
        boolean accept(Annotation[] annotations) throws ApiException;
    }
}
