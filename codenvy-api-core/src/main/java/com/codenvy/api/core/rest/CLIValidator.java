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
package com.codenvy.api.core.rest;

import com.codenvy.api.core.ApiException;
import com.codenvy.api.core.rest.annotations.Argument;
import com.codenvy.api.core.rest.annotations.CLI;
import com.codenvy.api.core.rest.annotations.Option;
import com.codenvy.api.core.util.ClassUtil;

import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validates CLI classes.
 *
 * @author Eugene Voevodin
 */
public final class CLIValidator {

    private static final Set<Class<? extends Annotation>> JAX_RS_PARAM_ANNOTATIONS;

    static {
        List<Class<? extends Annotation>> tmpAnnotations = new ArrayList<>(7);
        tmpAnnotations.add(QueryParam.class);
        tmpAnnotations.add(HeaderParam.class);
        tmpAnnotations.add(FormParam.class);
        tmpAnnotations.add(PathParam.class);
        tmpAnnotations.add(CookieParam.class);
        tmpAnnotations.add(MatrixParam.class);
        JAX_RS_PARAM_ANNOTATIONS = new HashSet<>(tmpAnnotations);
    }

    private CLIValidator() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static boolean isValid(Class<? extends Service> clazz) {
        try {
            validate(clazz);
            return true;
        } catch (ApiException e) {
            return false;
        }
    }

    public static void validate(Class<? extends Service> clazz) throws ApiException {
        if (clazz == null) {
            throw new IllegalArgumentException("Given class should not be a null");
        }
        /*CLASS*/
        checkClassAnnotatedWith(clazz, CLI.class);
        checkClassAnnotatedWith(clazz, Path.class);
        /*METHODS*/
        checkEachCLIMethodHasItOwnCommand(clazz);
        checkEachCLIMethodIsAnnotatedWithPath(clazz);
        validateEachCLIMethodArguments(clazz);
        /*FIELDS*/
        validateEachCLIField(clazz);
    }

    private static void validateEachCLIField(Class<? extends Service> clazz) throws ApiException {
        final Set<Integer> argumentPositions = new HashSet<>();
        for (Field field : clazz.getFields()) {
            checkItIsNoConflictsBetweenOptionsAndArguments(field.getAnnotations());
            checkArgumentPosition(field.getAnnotations(), argumentPositions);
        }
    }

    private static void checkArgumentPosition(Annotation[] annotations, Set<Integer> positionsMemory) throws ApiException {
        Argument cliArgument = ClassUtil.getAnnotationIfPresent(annotations, Argument.class);
        if (cliArgument != null && cliArgument.value() > 0 && !positionsMemory.add(cliArgument.value())) {
            throw new ApiException(
                    String.format("Same parameters should not have same positions, but they have at position %d", cliArgument.value()));
        }
    }

    private static void validateEachCLIMethodArguments(Class<?> clazz) throws ApiException {
        final Set<Integer> cliArgumentPositions = new HashSet<>();
        for (Method method : clazz.getMethods()) {
            if (ClassUtil.getMethodAnnotation(method, CLI.class) != null) {
                for (Annotation[] parameterAnnotations : method.getParameterAnnotations()) {
                    checkItIsNoConflictsBetweenOptionsAndArguments(parameterAnnotations);
                    checkArgumentPosition(parameterAnnotations, cliArgumentPositions);
                }
            }
        }
    }

    private static void checkItIsNoConflictsBetweenOptionsAndArguments(Annotation[] annotations) throws ApiException {
        Argument argument = ClassUtil.getAnnotationIfPresent(annotations, Argument.class);
        Option option = ClassUtil.getAnnotationIfPresent(annotations, Option.class);
        boolean isRESTParameterPresent = isRESTParamAnnotationPresent(annotations);
        if (argument != null && option != null) {
            throw new ApiException(String.format("%s %s should not be declared gather", Argument.class.getName(), Option.class.getName()));
        } else if ((argument != null || option != null) && !isRESTParameterPresent) {
            throw new ApiException(String.format("%s should be declared with any JAX-RS parameter annotation",
                                                 argument == null ? option.getClass().getName() : argument.getClass().getName()));
        }
    }

    private static void checkEachCLIMethodHasItOwnCommand(Class<?> clazz) throws ApiException {
        Set<String> commands = new HashSet<>();
        CLI current;
        for (Method method : clazz.getMethods()) {
            if ((current = ClassUtil.getMethodAnnotation(method, CLI.class)) != null) {
                if (!commands.contains(current.value())) {
                    commands.add(current.value());
                } else {
                    throw new ApiException(String.format("Command %s already exists", current.value()));
                }
            }
        }
    }

    private static void checkEachCLIMethodIsAnnotatedWithPath(Class<?> clazz) throws ApiException {
        for (Method method : clazz.getMethods()) {
            if (ClassUtil.getMethodAnnotation(method, CLI.class) != null && ClassUtil.getMethodAnnotation(method, Path.class) == null) {
                throw new ApiException(
                        String.format("Each method annotated with CLI should be annotated with Path, but %s doesn't", method.getName()));
            }
        }
    }

    private static void checkClassAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotation) throws ApiException {
        if (ClassUtil.getClassAnnotation(clazz, annotation) == null) {
            throw new ApiException(String.format("Class %s is not annotated with %s", clazz.getName(), annotation.getName()));
        }
    }

    private static boolean isRESTParamAnnotationPresent(Annotation[] sourceAnnotations) {
        for (Annotation source : sourceAnnotations) {
            for (Class target : JAX_RS_PARAM_ANNOTATIONS) {
                if (source.annotationType().equals(target)) {
                    return true;
                }
            }
        }
        return false;
    }
}