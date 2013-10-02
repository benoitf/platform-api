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

import com.codenvy.api.vfs.shared.dto.VirtualFileSystemInfo;

import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.Identity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Gives access to the current user context, e.g. uses HttpServletRequest to get info about Principal.
 *
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 */
public abstract class VirtualFileSystemUserContext {

    protected VirtualFileSystemUserContext() {
    }

    /** Get current user. */
    public abstract VirtualFileSystemUser getVirtualFileSystemUser();

    // TODO: Temporary solution, need improve it.
    public static VirtualFileSystemUserContext newInstance() {
        return new DefaultVirtualFileSystemUserContext();
    }

    private static class DefaultVirtualFileSystemUserContext extends VirtualFileSystemUserContext {
        public VirtualFileSystemUser getVirtualFileSystemUser() {
            final ConversationState cs = ConversationState.getCurrent();

            if (cs == null) {
                return new VirtualFileSystemUser(VirtualFileSystemInfo.ANONYMOUS_PRINCIPAL, Collections.<String>emptySet());
            }
            final Identity identity = cs.getIdentity();
            final Set<String> groups = new HashSet<>(2);
            if (identity.getRoles().contains("developer")) {
                groups.add("workspace/developer");
            }
            if (identity.getRoles().contains("admin")) {
                groups.add("workspace/admin");
            }
            return new VirtualFileSystemUser(identity.getUserId(), groups);
        }
    }
}
