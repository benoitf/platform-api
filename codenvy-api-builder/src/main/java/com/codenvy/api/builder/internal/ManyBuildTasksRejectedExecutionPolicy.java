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
package com.codenvy.api.builder.internal;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/** @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a> */
public final class ManyBuildTasksRejectedExecutionPolicy implements RejectedExecutionHandler {
    private final RejectedExecutionHandler delegate;

    public ManyBuildTasksRejectedExecutionPolicy(RejectedExecutionHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (executor.getPoolSize() >= executor.getCorePoolSize()) {
            throw new RejectedExecutionException("Too many builds in progress ");
        }
        delegate.rejectedExecution(r, executor);
    }
}
