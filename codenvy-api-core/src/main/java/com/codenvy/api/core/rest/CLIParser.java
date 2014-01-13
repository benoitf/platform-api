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
import com.codenvy.api.core.rest.annotations.Description;
import com.codenvy.api.core.rest.annotations.Option;
import com.codenvy.api.core.rest.annotations.Required;
import com.codenvy.api.core.rest.annotations.Valid;
import com.codenvy.api.core.rest.shared.JAXRSParameterType;
import com.codenvy.api.core.rest.shared.dto.CLIArgument;
import com.codenvy.api.core.rest.shared.dto.CLIBase;
import com.codenvy.api.core.util.ClassUtil;
import com.codenvy.dto.server.DtoFactory;

import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO it is not done yet
 *
 * @author Eugene Voevodin
 */
public final class CLIParser {

    private static final Set<Class<? extends Annotation>> JAX_RS_PARAM_ANNOTATIONS;

    private static final ClassUtil.AnnotationsFilter ARGUMENTS_FILTER;

    private static final ClassUtil.AnnotationsFilter OPTIONS_FILTER;

    static {
        List<Class<? extends Annotation>> tmpAnnotations = new ArrayList<>(7);
        tmpAnnotations.add(QueryParam.class);
        tmpAnnotations.add(HeaderParam.class);
        tmpAnnotations.add(FormParam.class);
        tmpAnnotations.add(PathParam.class);
        tmpAnnotations.add(CookieParam.class);
        tmpAnnotations.add(MatrixParam.class);
        JAX_RS_PARAM_ANNOTATIONS = new HashSet<>(tmpAnnotations);
        ARGUMENTS_FILTER = new ClassUtil.AnnotationsFilter() {
            @Override
            public boolean accept(Annotation[] annotations) throws ApiException {
                return ClassUtil.getAnnotationIfPresent(annotations, Argument.class) != null;
            }
        };
        OPTIONS_FILTER = new ClassUtil.AnnotationsFilter() {
            @Override
            public boolean accept(Annotation[] annotations) throws ApiException {
                return ClassUtil.getAnnotationIfPresent(annotations, Option.class) != null;
            }
        };
    }


    public CLIParser() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static CLIBase parse(Class<? extends Service> clazz) throws ApiException {
        CLIValidator.validate(clazz);
        CLIBase cliBase = DtoFactory.getInstance().createDto(CLIBase.class);
        List<Field> fieldsAnnotatedWithArgument = ClassUtil.getFields(clazz, ARGUMENTS_FILTER);
        List<CLIArgument> classArguments = new ArrayList<>(fieldsAnnotatedWithArgument.size());
        for (Field field : fieldsAnnotatedWithArgument) {
            classArguments.add(parseCLIArgument(field.getAnnotations(), field.getType()));
        }
        //todo add options and commands
        return cliBase;
    }

    private static CLIArgument parseCLIArgument(Annotation[] annotations, Class<?> type) throws ApiException {
        CLIArgument cliArgument =
                DtoFactory.getInstance().createDto(CLIArgument.class).withType(ClassUtil.getParameterType(type));
        Argument argument = ClassUtil.getAnnotationIfPresent(annotations, Argument.class);
        Description description = ClassUtil.getAnnotationIfPresent(annotations, Description.class);
        Valid valid = ClassUtil.getAnnotationIfPresent(annotations, Valid.class);
        Required required = ClassUtil.getAnnotationIfPresent(annotations, Required.class);
        //can not be null in this case, cause service was validated before
        Annotation restParameter = getJAXRSParamAnnotationIfPresent(annotations);
        cliArgument.setPosition(argument.value());
        cliArgument.setRequired(required != null);
        cliArgument.setRestParameterType(getJAXRSParameterType(restParameter));
        cliArgument.setParameterValue(getJAXRSParameterAnnotationValue(restParameter));
        if (description != null) {
            cliArgument.setDescription(description.value());
        }
        if (valid != null) {
            cliArgument.setValid(Arrays.asList(valid.value()));
        }
        return cliArgument;
    }

//    private static CLIOption parseCLIOption(Annotation[] annotations, Class<?> type) {
//        CLIOption cliOption =
//                DtoFactory.getInstance().createDto(CLIOption.class).withType(ClassUtil.getParameterType(type));
//        todo
//        return cliOption;
//    }

    private static Annotation getJAXRSParamAnnotationIfPresent(Annotation[] source) {
        Annotation current;
        for (Class target : JAX_RS_PARAM_ANNOTATIONS) {
            if ((current = ClassUtil.getAnnotationIfPresent(source, target)) != null) {
                return current;
            }
        }
        return null;
    }

    private static JAXRSParameterType getJAXRSParameterType(Annotation parameterAnnotation) throws ApiException {
        Class<? extends Annotation> annotationType = parameterAnnotation.annotationType();
        if (annotationType == QueryParam.class) {
            return JAXRSParameterType.QUERY;
        } else if (annotationType == HeaderParam.class) {
            return JAXRSParameterType.HEADER;
        } else if (annotationType == PathParam.class) {
            return JAXRSParameterType.PATH;
        } else if (annotationType == FormParam.class) {
            return JAXRSParameterType.FORM;
        } else if (annotationType == MatrixParam.class) {
            return JAXRSParameterType.MATRIX;
        } else if (annotationType == CookieParam.class) {
            return JAXRSParameterType.COOKIE;
        } else {
            throw new IllegalArgumentException(
                    String.format("Given annotation should be JAX-RS parameter, but %s is not", annotationType.getName()));
        }
    }

    private static String getJAXRSParameterAnnotationValue(Annotation parameterAnnotation) {
        Class annotationType = parameterAnnotation.annotationType();
        if (annotationType == QueryParam.class) {
            return ((QueryParam)parameterAnnotation).value();
        } else if (annotationType == HeaderParam.class) {
            return ((HeaderParam)parameterAnnotation).value();
        } else if (annotationType == PathParam.class) {
            return ((PathParam)parameterAnnotation).value();
        } else if (annotationType == FormParam.class) {
            return ((FormParam)parameterAnnotation).value();
        } else if (annotationType == MatrixParam.class) {
            return ((MatrixParam)parameterAnnotation).value();
        } else if (annotationType == CookieParam.class) {
            return ((CookieParam)parameterAnnotation).value();
        } else {
            throw new IllegalArgumentException(
                    String.format("Given annotation should be JAX-RS parameter, but %s is not", annotationType.getName()));
        }
    }
}