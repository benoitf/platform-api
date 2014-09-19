/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.api.core.rest.shared.dto;

import com.codenvy.api.core.rest.shared.Links;
import com.codenvy.dto.shared.DTO;
import com.codenvy.dto.shared.DelegateRule;
import com.codenvy.dto.shared.DelegateTo;

import java.util.List;

/**
 * @author andrew00x
 */
@DTO
public interface Hyperlinks {
    List<Link> getLinks();

    Hyperlinks withLinks(List<Link> links);

    void setLinks(List<Link> links);

    @DelegateTo(client = @DelegateRule(type = Links.class, method = "getLinks"),
                server = @DelegateRule(type = Links.class, method = "getLinks"))
    List<Link> getLinks(String rel);

    @DelegateTo(client = @DelegateRule(type = Links.class, method = "getLink"),
                server = @DelegateRule(type = Links.class, method = "getLink"))
    Link getLink(String rel);
}
