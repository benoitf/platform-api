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
package com.codenvy.api.core.rest.shared.dto;

import com.codenvy.dto.shared.DTO;

import java.util.List;

/**
 * Describes CLI command.
 *
 * @author Eugene Voevodin
 * @see com.codenvy.api.core.rest.shared.dto.CLIArgument
 * @see com.codenvy.api.core.rest.shared.dto.CLIOption
 */
@DTO
public interface CLICommand {

    String getCommand();

    void setCommand(String command);

    CLICommand withCommand(String command);

    List<CLIArgument> getArguments();

    void setArguments(List<CLIArgument> arguments);

    CLICommand withArguments(List<CLIArgument> arguments);

    List<CLIOption> getOptions();

    void setOptions(List<CLIOption> options);

    CLICommand withOptions(List<CLIOption> options);

    String getPath();

    void setPath(String path);

    CLICommand withPath(String path);

    String getMethod();

    void setMethod(String method);

    CLICommand withMethod(String method);

    String getProduces();

    void setProduces(String media);

    CLICommand withProduces(String media);

    String getConsumes();

    void setConsumes(String media);

    CLICommand withConsumes(String media);
}
