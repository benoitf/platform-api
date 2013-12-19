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

import com.codenvy.api.vfs.server.exceptions.VirtualFileSystemException;
import com.codenvy.api.vfs.server.observation.EventListenerList;
import com.codenvy.api.vfs.shared.dto.VirtualFileSystemInfo;
import com.codenvy.commons.env.EnvironmentContext;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Provides access to virtual file systems which have registered providers in VirtualFileSystemRegistry.
 *
 * @author andrew00x
 */
@Path("{ws-name}/vfs")
public class VirtualFileSystemFactory {
    @Inject
    private VirtualFileSystemRegistry registry;

    @Inject
    @Nullable
    private EventListenerList listeners;

    @Inject
    @Nullable
    private RequestValidator requestValidator;

    @Context
    private Providers providers;

    @Context
    private javax.servlet.http.HttpServletRequest request;

    @Path("v2")
    public VirtualFileSystem getFileSystem() throws VirtualFileSystemException {
        validateRequest();
        final String vfsId = (String)EnvironmentContext.getCurrent().getVariable(EnvironmentContext.WORKSPACE_ID);
        VirtualFileSystemProvider provider = registry.getProvider(vfsId);
        return provider.newInstance(getContext(), listeners);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<VirtualFileSystemInfo> getAvailableFileSystems() throws VirtualFileSystemException {
        validateRequest();
        Collection<VirtualFileSystemProvider> vfsProviders = registry.getRegisteredProviders();
        List<VirtualFileSystemInfo> result = new ArrayList<>(vfsProviders.size());
        RequestContext context = getContext();
        for (VirtualFileSystemProvider p : vfsProviders) {
            VirtualFileSystem fs = p.newInstance(context, listeners);
            result.add(fs.getInfo());
        }
        return result;
    }

    private void validateRequest() {
        if (requestValidator != null) {
            requestValidator.validate(request);
        }
    }

    protected RequestContext getContext() {
        ContextResolver<RequestContext> contextResolver = providers.getContextResolver(RequestContext.class, null);
        if (contextResolver != null) {
            return contextResolver.getContext(RequestContext.class);
        }
        return null;
    }
}
