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
package com.codenvy.api.builder.internal;

import java.io.IOException;

/**
 * Manages build sources.
 *
 * @author andrew00x
 * @author Eugene Voevodin
 */
public interface SourcesManager {
    /**
     * Get build sources. Sources are copied to the directory <code>workDir</code>.
     *
     * @param workspace
     *         workspace
     * @param project
     *         project
     * @param sourcesUrl
     *         sources url
     * @param workDir
     *         directory where sources will be copied
     */
    void getSources(String workspace, String project, String sourcesUrl, java.io.File workDir) throws IOException;

    java.io.File getDirectory();

    boolean addListener(SourceManagerListener listener);

    boolean removeListener(SourceManagerListener listener);
}
