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
package com.codenvy.api.core.rest.shared.dto;

import com.codenvy.api.core.rest.shared.JAXRSParameterType;
import com.codenvy.api.core.rest.shared.ParameterType;
import com.codenvy.dto.shared.DTO;

import java.util.List;

/**
 * @author Eugene Voevodin
 */
@DTO
public interface CLIParameter {
    ParameterType getType();

    void setType(ParameterType type);

    CLIParameter withType(ParameterType type);

    String getDescription();

    void setDescription(String description);

    CLIParameter withDescription(String description);

    List<String> getValid();

    void setValid(List<String> valid);

    CLIParameter withValid(List<String> valid);

    boolean isRequired();

    void setRequired(boolean isRequired);

    CLIParameter withRequired(boolean isRequired);

    JAXRSParameterType getParameterType();

    void setParameterType(JAXRSParameterType parameterType);

    CLIParameter withParameterType(JAXRSParameterType parameterType);

    String getParameterName();

    void setParameterName(String parameterName);

    CLIParameter withParameterName(String parameterName);
}
