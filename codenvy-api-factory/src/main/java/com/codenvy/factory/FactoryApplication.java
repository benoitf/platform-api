/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [2013] Codenvy, S.A.
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
package com.codenvy.factory;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
import com.codenvy.factory.services.FactoryService;

/** Provide a set of factory REST services */
public class FactoryApplication extends Application {

    private final Set<Class<?>> classes;

    private final Set<Object> objects;

    public FactoryApplication() {
        classes = new HashSet<>();
        classes.add(FactoryService.class);
        objects = new HashSet<>(0);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        return objects;
    }

}