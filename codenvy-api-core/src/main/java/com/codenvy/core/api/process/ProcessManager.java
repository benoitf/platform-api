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
package com.codenvy.core.api.process;

import com.codenvy.core.api.util.SystemInfo;

/** @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a> */
abstract class ProcessManager {
    static ProcessManager newInstance() {
        if (SystemInfo.isUnix()) {
            return new UnixProcessManager();
        }
        return new DefaultProcessManager();
    }

    abstract void kill(Process process);

    abstract void kill(int pid);

    abstract boolean isAlive(Process process);

    abstract boolean isAlive(int pid);

    abstract int getPid(Process process);
}
