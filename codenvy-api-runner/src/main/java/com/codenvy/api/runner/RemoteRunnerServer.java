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
package com.codenvy.api.runner;

import com.codenvy.api.core.rest.HttpJsonHelper;
import com.codenvy.api.core.rest.RemoteException;
import com.codenvy.api.core.rest.RemoteServiceDescriptor;
import com.codenvy.api.core.rest.shared.dto.Link;
import com.codenvy.api.runner.internal.Constants;
import com.codenvy.api.runner.internal.dto.ServerState;
import com.codenvy.api.runner.internal.dto.RunnerDescriptor;
import com.codenvy.api.runner.internal.dto.RunnerList;

import java.io.IOException;
import java.util.List;

/**
 * Factory for RemoteRunner. See {@link RemoteRunner} about usage of this class.
 *
 * @author andrew00x
 */
public class RemoteRunnerServer extends RemoteServiceDescriptor {

    /** Name of IDE workspace this server used for. */
    private String assignedWorkspace;
    /** Name of project inside IDE workspace this server used for. */
    private String assignedProject;

    public RemoteRunnerServer(String baseUrl) {
        super(baseUrl);
    }

    public String getAssignedWorkspace() {
        return assignedWorkspace;
    }

    public void setAssignedWorkspace(String assignedWorkspace) {
        this.assignedWorkspace = assignedWorkspace;
    }

    public String getAssignedProject() {
        return assignedProject;
    }

    public void setAssignedProject(String assignedProject) {
        this.assignedProject = assignedProject;
    }

    public boolean isDedicated() {
        return assignedWorkspace != null;
    }

    public RemoteRunner getRemoteRunner(String name) throws RunnerException {
        try {
            for (RunnerDescriptor runnerDescriptor : getAvailableRunners()) {
                if (name.equals(runnerDescriptor.getName())) {
                    return new RemoteRunner(baseUrl, runnerDescriptor, getLinks());
                }
            }
        } catch (IOException e) {
            throw new RunnerException(e);
        } catch (RemoteException e) {
            throw new RunnerException(e.getServiceError());
        }
        throw new RunnerException(String.format("Invalid runner name %s", name));
    }

    public RemoteRunner createRemoteRunner(RunnerDescriptor descriptor) throws RunnerException {
        try {
            return new RemoteRunner(baseUrl, descriptor, getLinks());
        } catch (IOException e) {
            throw new RunnerException(e);
        } catch (RemoteException e) {
            throw new RunnerException(e.getServiceError());
        }
    }

    public List<RunnerDescriptor> getAvailableRunners() throws RunnerException {
        try {
            final Link link = getLink(Constants.LINK_REL_AVAILABLE_RUNNERS);
            if (link == null) {
                throw new RunnerException("Unable get URL for retrieving list of remote runners");
            }
            return HttpJsonHelper.request(RunnerList.class, link).getRunners();
        } catch (IOException e) {
            throw new RunnerException(e);
        } catch (RemoteException e) {
            throw new RunnerException(e.getServiceError());
        }
    }

    public ServerState getServerState() throws RunnerException {
        try {
            final Link stateLink = getLink(Constants.LINK_REL_SERVER_STATE);
            if (stateLink == null) {
                throw new RunnerException(String.format("Unable get URL for getting state of a remote server '%s'", baseUrl));
            }
            return HttpJsonHelper.request(ServerState.class, stateLink);
        } catch (IOException e) {
            throw new RunnerException(e);
        } catch (RemoteException e) {
            throw new RunnerException(e.getServiceError());
        }
    }
}
