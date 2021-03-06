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
package com.codenvy.api.project.server;

/** @author andrew00x */
public class Constants {
    // rels for known project links
    public static final String LINK_REL_GET_PROJECTS   = "get projects";
    public static final String LINK_REL_CREATE_PROJECT = "create project";
    public static final String LINK_REL_UPDATE_PROJECT = "update project";
    public static final String LINK_REL_EXPORT_ZIP     = "zipball sources";
    public static final String LINK_REL_CHILDREN       = "children";
    public static final String LINK_REL_TREE           = "tree";
    public static final String LINK_REL_DELETE         = "delete";
    public static final String LINK_REL_GET_CONTENT    = "get content";
    public static final String LINK_REL_UPDATE_CONTENT = "update content";

    public static final String LINK_REL_PROJECT_TYPES = "project types";

    public static final String CODENVY_FOLDER                     = ".codenvy";
    public static final String CODENVY_PROJECT_FILE               = "project";
    public static final String CODENVY_PROJECT_FILE_RELATIVE_PATH = CODENVY_FOLDER + "/" + CODENVY_PROJECT_FILE;

    private Constants() {
    }
}
