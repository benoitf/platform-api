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
package com.codenvy.api.auth.shared.dto;

import com.codenvy.dto.shared.DTO;

/**
 * @author gazarenkov
 */
@DTO
public interface Credentials {

    String getRealm();

    void setRealm(String realm);

    Credentials withRealm(String realm);

    String getUsername();

    void setUsername(String name);

    Credentials withUsername(String name);

    String getPassword();

    void setPassword(String password);

    Credentials withPassword(String password);

}
