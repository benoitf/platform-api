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
package com.codenvy.api.core.rest.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes CLI argument. If any {@link com.codenvy.api.core.rest.Service} is annotated with
 * {@link com.codenvy.api.core.rest.annotations.CLI} it can have fields and method parameters annotated with
 * {@link com.codenvy.api.core.rest.annotations.Argument}. Any field or option that annotated with
 * {@link com.codenvy.api.core.rest.annotations.Argument} should be annotated with any JAX-RS parameter annotation, i.e
 * {@link javax.ws.rs.QueryParam}.
 *
 * @author Eugene Voevodin
 * @see com.codenvy.api.core.rest.shared.dto.CLIArgument
 * @see com.codenvy.api.core.rest.annotations.Option
 * @see com.codenvy.api.core.rest.annotations.CLI
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Argument {
    /** argument position */
    int value() default -1;
}