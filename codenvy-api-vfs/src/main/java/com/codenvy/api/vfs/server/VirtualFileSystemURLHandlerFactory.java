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
package com.codenvy.api.vfs.server;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

/**
 * Factory for URLStreamHandler to <code>ide+vfs</code> protocol.
 *
 * @author <a href="mailto:aparfonov@exoplatform.com">Andrey Parfonov</a>
 */
public final class VirtualFileSystemURLHandlerFactory implements URLStreamHandlerFactory {
    private final URLStreamHandlerFactory delegate;

    private final VirtualFileSystemRegistry registry;

    /**
     * @param delegate
     *         factory which we should ask to create URLStreamHandler if current factory does not support
     *         requested protocol.
     * @param registry
     *         set of all available virtual file systems
     */
    public VirtualFileSystemURLHandlerFactory(URLStreamHandlerFactory delegate, VirtualFileSystemRegistry registry) {
        this.delegate = delegate;
        this.registry = registry;
    }

    /** @see java.net.URLStreamHandlerFactory#createURLStreamHandler(java.lang.String) */
    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if ("ide+vfs".equals(protocol)) {
            return new VirtualFileSystemResourceHandler(registry);
        } else if (delegate != null) {
            delegate.createURLStreamHandler(protocol);
        }
        return null;
    }
}
