/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
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
package com.codenvy.api.vfs.server.observation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author andrew00x
 */
public final class LogErrorHandler implements ErrorHandler {
    private static final Logger LOG = LoggerFactory.getLogger(LogErrorHandler.class);

    @Override
    public void onError(VirtualFileEvent event, Throwable error) {
        LOG.error(String.format("Error processing of event: %s", event), error);
    }
}


