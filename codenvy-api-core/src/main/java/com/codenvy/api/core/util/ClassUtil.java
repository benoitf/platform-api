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

import com.codenvy.api.core.rest.shared.ParameterType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;

/**
 * Help to do some external operations {@link java.lang.Class}
 *
 * @author Eugene Voevodin
 */
public final class ClassUtil {

    private ClassUtil() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    /**
     * Converts any given {@link java.lang.Class} to {@link com.codenvy.api.core.rest.shared.ParameterType}
     */
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

    /**
     * Searches for annotation
     *
     * @param sourceAnnotation
     *         where make search
     * @param target
     *         annotation to search
     * @return found {@link java.lang.annotation.Annotation} or {@code null} if it is not exists in sources
     */
    public static <T extends Annotation> T getAnnotationIfPresent(Annotation[] sourceAnnotation, Class<T> target) {
        for (Annotation source : sourceAnnotation) {
            if (source.annotationType().equals(target)) {
                return (T)source;
            }
        }
        return null;
    }

    /**
     * Searches for the class annotation in inheritance tree from given class to Object
     *
     * @param clazz
     *         start point to search annotation
     * @param annotationClass
     *         annotation to search
     * @return {@link java.lang.annotation.Annotation} if found, or {@code null} if not
     */
    public static <T extends Annotation> T getClassAnnotation(Class<?> clazz, Class<T> annotationClass) {
        T annotation = clazz.getAnnotation(annotationClass);
        if (annotation == null) {
            for (Class<?> c = clazz.getSuperclass(); annotation == null && c != null && c != Objects.class; c = c.getSuperclass()) {
                annotation = c.getAnnotation(annotationClass);
            }
        }
        return annotation;
    }

    /**
     * Searches for the method annotation in inheritance tree while annotation will not be found or method present
     *
     * @param method
     *         start method to search annotation
     * @param annotationClass
     *         annotation to search
     * @return {@link java.lang.annotation.Annotation} if found, or {@code null} if not
     */
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

    /**
     * Searches for all class methods that accepted with given filter
     *
     * @param clazz
     *         where to search
     * @param filter
     *         accepts annotations
     * @return list of filtered methods
     */
    public static List<Method> getMethods(Class<?> clazz, AnnotationsFilter filter) {
        List<Method> filteredMethods = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            Set<Annotation> allMethodAnnotations = getAllMethodAnnotations(method);
            if (filter.accept(allMethodAnnotations.toArray(new Annotation[allMethodAnnotations.size()]))) {
                filteredMethods.add(method);
            }
        }
        return filteredMethods;
    }

    /**
     * Searches for all method annotations in inheritance tree while method is present
     *
     * @param method
     *         start point to search
     * @return set of found annotations
     */
    public static Set<Annotation> getAllMethodAnnotations(Method method) {
        Set<Annotation> annotations = new HashSet<>();
        for (Class<?> c = method.getDeclaringClass();
             c != null && c != Object.class;
             c = c.getSuperclass()) {
            Method inherited = null;
            try {
                inherited = c.getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException ignored) {
            }
            if (inherited != null) {
                annotations.addAll(Arrays.asList(inherited.getAnnotations()));
            }
        }
        return annotations;
    }

    /**
     * Searches for all fields that accepted with given filter
     *
     * @param clazz
     *         where to search
     * @param filter
     *         accepts annotations
     * @return
     */
    public static List<Field> getFields(Class<?> clazz, AnnotationsFilter filter) {
        List<Field> filteredFields = new ArrayList<>();
        for (Field field : clazz.getFields()) {
            if (filter.accept(field.getAnnotations())) {
                filteredFields.add(field);
            }
        }
        return filteredFields;
    }

    /**
     * Accepts annotations
     */
    public static interface AnnotationsFilter {
        boolean accept(Annotation[] annotations);
    }
}
