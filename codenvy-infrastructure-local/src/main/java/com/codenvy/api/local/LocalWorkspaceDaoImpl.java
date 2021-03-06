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
package com.codenvy.api.local;

import com.codenvy.api.workspace.server.dao.WorkspaceDao;
import com.codenvy.api.workspace.server.exception.WorkspaceException;
import com.codenvy.api.workspace.shared.dto.Workspace;
import com.codenvy.dto.server.DtoFactory;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

@Singleton
public class LocalWorkspaceDaoImpl implements WorkspaceDao {
    @Override
    public void create(Workspace workspace) throws WorkspaceException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void update(Workspace workspace) throws WorkspaceException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void remove(String id) throws WorkspaceException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Workspace getById(String id) throws WorkspaceException {
        return Constants.WORKSPACE;
    }

    @Override
    public Workspace getByName(String name) throws WorkspaceException {
        return Constants.WORKSPACE;

    }

    @Override
    public List<Workspace> getByOrganization(String organizationId) throws WorkspaceException {
        return Arrays.asList(Constants.WORKSPACE);
    }
}