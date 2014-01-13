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

import com.codenvy.api.core.rest.shared.JAXRSParameterType;
import com.codenvy.api.core.rest.shared.ParameterType;
import com.codenvy.dto.shared.DTO;

import java.util.List;

/**
 * Describes CLI argument.
 *
 * @author Eugene Voevodin
 * @see com.codenvy.api.core.rest.annotations.Description
 * @see com.codenvy.api.core.rest.annotations.Required
 * @see com.codenvy.api.core.rest.annotations.Valid
 * @see com.codenvy.api.core.rest.shared.JAXRSParameterType
 */
@DTO
public interface CLIArgument {

    int getPosition();

    void setPosition(int position);

    CLIArgument withPosition(int position);

    ParameterType getType();

    void setType(ParameterType type);

    CLIArgument withType(ParameterType type);

    String getDescription();

    void setDescription(String description);

    CLIArgument withDescription(String description);

    List<String> getValid();

    void setValid(List<String> valid);

    CLIArgument withValid(List<String> valid);

    boolean isRequired();

    void setRequired(boolean isRequired);

    CLIArgument withRequired(boolean isRequired);

    JAXRSParameterType getRestParameterType();

    void setRestParameterType(JAXRSParameterType parameterType);

    CLIArgument withRestParameterType(JAXRSParameterType parameterType);

    String getParameterValue();

    void setParameterValue(String parameterValue);

    CLIArgument withParameterValue(String parameterValue);
}
