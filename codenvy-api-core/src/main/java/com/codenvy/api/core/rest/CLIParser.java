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
import com.codenvy.api.core.rest.annotations.Description;
import com.codenvy.api.core.rest.annotations.Option;
import com.codenvy.api.core.rest.annotations.Required;
import com.codenvy.api.core.rest.annotations.Valid;
import com.codenvy.api.core.rest.shared.JAXRSParameterType;
import com.codenvy.api.core.rest.shared.dto.CLIArgument;
import com.codenvy.api.core.rest.shared.dto.CLIBase;
import com.codenvy.api.core.rest.shared.dto.CLICommand;
import com.codenvy.api.core.rest.shared.dto.CLIOption;
import com.codenvy.api.core.rest.shared.dto.CLIParameter;
import com.codenvy.api.core.util.ClassUtil;
import com.codenvy.dto.server.DtoFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    private static final ClassUtil.AnnotationsFilter CLI_FILTER;

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
            public boolean accept(Annotation[] annotations) {
                return ClassUtil.getAnnotationIfPresent(annotations, Argument.class) != null;
            }
        };
        OPTIONS_FILTER = new ClassUtil.AnnotationsFilter() {
            @Override
            public boolean accept(Annotation[] annotations) {
                return ClassUtil.getAnnotationIfPresent(annotations, Option.class) != null;
            }
        };
        CLI_FILTER = new ClassUtil.AnnotationsFilter() {
            @Override
            public boolean accept(Annotation[] annotations) {
                return ClassUtil.getAnnotationIfPresent(annotations, CLI.class) != null;
            }
        };
    }

    public CLIParser() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static CLIBase parse(Class<? extends Service> clazz) throws ApiException {
        CLIValidator.validate(clazz);
        CLIBase cliBase = DtoFactory.getInstance().createDto(CLIBase.class);
        //CLI class arguments
        List<CLIArgument> arguments = new ArrayList<>();
        for (Field field : ClassUtil.getFields(clazz, ARGUMENTS_FILTER)) {
            arguments.add(createCLIArgument(field.getAnnotations(), field.getType()));
        }
        //CLI class options
        List<CLIOption> options = new ArrayList<>();
        for (Field field : ClassUtil.getFields(clazz, OPTIONS_FILTER)) {
            options.add(createCLIOption(field.getAnnotations(), field.getType()));
        }
        cliBase.setArguments(arguments);
        cliBase.setOptions(options);
        //CLI class commands(methods)
        List<CLICommand> commands = new ArrayList<>();
        for (Method method : ClassUtil.getMethods(clazz, CLI_FILTER)) {
            commands.add(createCLICommand(method));
        }
        cliBase.setCommands(commands);
        return cliBase;
    }

    private static CLIArgument createCLIArgument(Annotation[] annotations, Class<?> type) {
        CLIArgument cliArgument = DtoFactory.getInstance().createDto(CLIArgument.class);
        Argument argument = ClassUtil.getAnnotationIfPresent(annotations, Argument.class);
        cliArgument.setPosition(argument.value());
        fillCLIParameter(cliArgument, annotations, type);
        return cliArgument;
    }

    private static CLIOption createCLIOption(Annotation[] annotations, Class<?> type) {
        CLIOption cliOption =
                DtoFactory.getInstance().createDto(CLIOption.class);
        Option option = ClassUtil.getAnnotationIfPresent(annotations, Option.class);
        cliOption.setValue(option.value());
        cliOption.setFullValue(option.fullValue());
        cliOption.setPair(option.isPair());
        fillCLIParameter(cliOption, annotations, type);
        return cliOption;
    }

    private static CLICommand createCLICommand(Method method) {
        CLICommand cliCommand = DtoFactory.getInstance().createDto(CLICommand.class);
        Path path = ClassUtil.getMethodAnnotation(method, Path.class);
        CLI cli = ClassUtil.getMethodAnnotation(method, CLI.class);
        Consumes consumes = ClassUtil.getMethodAnnotation(method, Consumes.class);
        Produces produces = ClassUtil.getMethodAnnotation(method, Produces.class);
        if (consumes != null) {
            cliCommand.setConsumes(Arrays.asList(consumes.value()));
        } else if (produces != null) {
            cliCommand.setProduces(Arrays.asList(produces.value()));
        }
        cliCommand.setCommand(cli.value());
        String httpMethod = null;
        if (ClassUtil.getMethodAnnotation(method, GET.class) != null) {
            httpMethod = "GET";
        } else if (ClassUtil.getMethodAnnotation(method, POST.class) != null) {
            httpMethod = "POST";
        } else if (ClassUtil.getMethodAnnotation(method, PUT.class) != null) {
            httpMethod = "PUT";
        } else if (ClassUtil.getMethodAnnotation(method, DELETE.class) != null) {
            httpMethod = "DELETE";
        }
        //analyzing method parameters
        Class<?>[] parameterTypes = method.getParameterTypes();
        List<CLIArgument> arguments = new ArrayList<>();
        List<CLIOption> options = new ArrayList<>();
        for (int i = 0; i < parameterTypes.length; i++) {
            Annotation[] parameterAnnotations = method.getParameterAnnotations()[i];
            if (ARGUMENTS_FILTER.accept(parameterAnnotations)) {
                arguments.add(createCLIArgument(parameterAnnotations, parameterTypes[i]));
            } else if (OPTIONS_FILTER.accept(parameterAnnotations)) {
                options.add(createCLIOption(parameterAnnotations, parameterTypes[i]));
            }
        }
        cliCommand.setArguments(arguments);
        cliCommand.setOptions(options);
        cliCommand.setMethod(httpMethod);
        return cliCommand;
    }

    private static void fillCLIParameter(CLIParameter cliParameter, Annotation[] annotations, Class<?> type) {
        cliParameter.setType(ClassUtil.getParameterType(type));
        Description description = ClassUtil.getAnnotationIfPresent(annotations, Description.class);
        Valid valid = ClassUtil.getAnnotationIfPresent(annotations, Valid.class);
        Required required = ClassUtil.getAnnotationIfPresent(annotations, Required.class);
        //can not be null in this case, cause service was validated before
        Annotation restParameter = getJAXRSParamAnnotationIfPresent(annotations);
        cliParameter.setRequired(required != null);
        cliParameter.setRestParameterType(getJAXRSParameterType(restParameter));
        cliParameter.setParameterValue(getJAXRSParameterAnnotationValue(restParameter));
        if (description != null) {
            cliParameter.setDescription(description.value());
        }
        if (valid != null) {
            cliParameter.setValid(Arrays.asList(valid.value()));
        }
    }

    private static Annotation getJAXRSParamAnnotationIfPresent(Annotation[] source) {
        Annotation current;
        for (Class target : JAX_RS_PARAM_ANNOTATIONS) {
            if ((current = ClassUtil.getAnnotationIfPresent(source, target)) != null) {
                return current;
            }
        }
        return null;
    }

    private static JAXRSParameterType getJAXRSParameterType(Annotation parameterAnnotation) {
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