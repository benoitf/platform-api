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
package com.codenvy.api.vfs.server.exceptions;

import com.codenvy.api.vfs.shared.ExitCodes;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/** @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a> */
@Provider
public class VirtualFileSystemRuntimeExceptionMapper implements ExceptionMapper<VirtualFileSystemRuntimeException> {
    /** @see javax.ws.rs.ext.ExceptionMapper#toResponse(Throwable) */
    @Override
    public Response toResponse(VirtualFileSystemRuntimeException exception) {
        return Response.status(500).entity(exception.getMessage()).type(MediaType.TEXT_PLAIN)
                       .header("X-Exit-Code", Integer.toString(ExitCodes.INTERNAL_ERROR)).build();
    }
}
