/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
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
package org.exoplatform.ide.vfs.shared;

/**
 * Interface describe object lock token.
 *
 * @author <a href="mailto:azatsarynnyy@exoplatform.org">Artem Zatsarynnyy</a>
 * @version $Id: LockToken.java Mar 27, 2012 10:34:26 AM azatsarynnyy $
 */
public interface LockToken {

    /**
     * Returns the lock token.
     *
     * @return the lock token
     */
    public String getLockToken();

    /**
     * Set the lock token.
     *
     * @param token
     *         the lock token
     */
    public void setLockToken(String token);

}