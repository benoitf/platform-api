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
 * Describes mapping for CLI classes.
 *
 * @author Eugene Voevodin
 * @see com.codenvy.api.core.rest.annotations.Argument
 * @see com.codenvy.api.core.rest.annotations.Option
 */
@DTO
public interface CLIBase {

    String getCommand();

    void setCommand(String command);

    CLIBase withCommand(String command);

    String getDescription();

    void setDescription(String description);

    CLIBase withDescription(String description);

    List<CLICommand> getCommands();

    void setCommands(List<CLICommand> commands);

    CLIBase withCommands(List<CLICommand> commands);

    String getPath();

    void setPath(String path);

    CLIBase withPath(String path);

    void setArguments(List<CLIArgument> arguments);

    List<CLIArgument> getArguments();

    CLIBase withArguments(List<CLIArgument> arguments);

    void setOptions(List<CLIOption> options);

    List<CLIOption> getOptions();

    CLIBase withOptions(List<CLIOption> options);
}