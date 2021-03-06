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
package com.codenvy.api.workspace.shared.dto;

import com.codenvy.api.core.rest.shared.dto.Link;
import com.codenvy.dto.shared.DTO;

import java.util.List;

/**
 * Describes workspace membership
 *
 * @author Eugene Voevodin
 * @see com.codenvy.api.workspace.shared.dto.Workspace
 */
@DTO
public interface Membership {

    WorkspaceRef getWorkspaceRef();

    void setWorkspaceRef(WorkspaceRef ref);

    Membership withWorkspaceRef(WorkspaceRef ref);

    List<String> getRoles();

    void setRoles(List<String> roles);

    Membership withRoles(List<String> roles);

    Link getUserLink();

    void setUserLink(Link link);

    Membership withUserLink(Link link);
}
