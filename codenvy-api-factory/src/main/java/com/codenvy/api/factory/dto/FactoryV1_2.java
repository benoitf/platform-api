package com.codenvy.api.factory.dto;

import com.codenvy.api.factory.parameter.FactoryParameter;
import com.codenvy.dto.shared.DTO;

import static com.codenvy.api.factory.parameter.FactoryParameter.Obligation.OPTIONAL;

/**
 * Factory of version 1.2
 *
 * @author Sergii Kabashniuk
 */
@DTO
public interface FactoryV1_2 extends FactoryV1_1 {
    /**
     * @return additinal git configuration
     */
    @FactoryParameter(obligation = OPTIONAL, queryParameterName = "git")
    Git getGit();

    void setGit(Git git);

    FactoryV1_2 withGit(Git git);

    /**
     * @return Factory acceptance restrictions
     */
    @FactoryParameter(obligation = OPTIONAL, queryParameterName = "restriction", trackedOnly = true)
    Restriction getRestriction();

    void setRestriction(Restriction restriction);

    FactoryV1_2 withRestriction(Restriction restriction);
}
