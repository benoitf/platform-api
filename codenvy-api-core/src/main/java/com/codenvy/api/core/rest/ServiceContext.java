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
package com.codenvy.api.core.rest;

import javax.ws.rs.core.UriBuilder;

/**
 * Helps to deliver context of RESTful request to components.
 *
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 */
public interface ServiceContext {
    /**
     * Get UriBuilder which already contains base URI of RESTful application and URL pattern of RESTful service that produces this
     * instance.
     */
    UriBuilder getServiceUriBuilder();

    /** Get UriBuilder which already contains base URI of RESTful application. */
    UriBuilder getBaseUriBuilder();
}
