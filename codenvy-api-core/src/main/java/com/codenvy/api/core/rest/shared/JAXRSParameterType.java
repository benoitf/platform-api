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
package com.codenvy.api.core.rest.shared;

/**
 * Describes REST parameter types. Used to implement JAX-RS parameter annotations in DTO object.
 *
 *
 * @author Eugene Voevodin
 * @see javax.ws.rs.QueryParam
 * @see javax.ws.rs.PathParam
 * @see javax.ws.rs.HeaderParam
 * @see javax.ws.rs.CookieParam
 * @see javax.ws.rs.MatrixParam
 * @see javax.ws.rs.FormParam
 */
public enum JAXRSParameterType {

    QUERY {
        @Override
        public String toString() {
            return "query";
        }
    },
    PATH {
        @Override
        public String toString() {
            return "path";
        }
    },
    HEADER {
        @Override
        public String toString() {
            return "header";
        }
    },
    COOKIE {
        @Override
        public String toString() {
            return "cookie";
        }
    },
    MATRIX {
        @Override
        public String toString() {
            return "matrix";
        }
    },
    FORM {
        @Override
        public String toString() {
            return "form";
        }
    }

}