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
 * Describes CLI option.
 *
 * @author Eugene Voevodin
 * @see com.codenvy.api.core.rest.annotations.Description
 * @see com.codenvy.api.core.rest.annotations.Required
 * @see com.codenvy.api.core.rest.annotations.Valid
 * @see com.codenvy.api.core.rest.shared.JAXRSParameterType
 */
@DTO
public interface CLIOption {

    String getValue();

    void setValue(String value);

    CLIOption withValue(String value);

    String getFullValue();

    void setFullValue(String value);

    CLIOption withFullValue(String value);

    boolean isPair();

    void setPair(boolean isPair);

    CLIOption withPair(boolean isPair);

    ParameterType getType();

    void setType(ParameterType type);

    CLIOption withType(ParameterType type);

    String getDescription();

    void setDescription(String description);

    CLIOption withDescription(String description);

    List<String> getValid();

    void setValid(List<String> valid);

    CLIOption withValid(List<String> valid);

    boolean isRequired();

    void setRequired(boolean isRequired);

    CLIOption withRequired(boolean isRequired);

    JAXRSParameterType getRestParameterType();

    void setRestParameterType(JAXRSParameterType parameterType);

    CLIOption withRestParameterType(JAXRSParameterType parameterType);

    String getParameterName();

    void setParameterName(String parameterName);

    CLIOption withParameterName(String parameterName);


}