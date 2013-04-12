/*
 * Copyright (C) 2010 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.ide.vfs.server;

import org.exoplatform.ide.vfs.server.exceptions.*;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class VirtualFileSystemApplication extends Application {
    private final Set<Object> singletons;

    private final Set<Class<?>> classes;

    public VirtualFileSystemApplication() {
        classes = new HashSet<Class<?>>(3);
        classes.add(VirtualFileSystemFactory.class);
        classes.add(RequestContextResolver.class);
        classes.add(NoCacheJsonWriter.class);
        singletons = new HashSet<Object>(10);
        singletons.add(new ContentStreamWriter());
        singletons.add(new ConstraintExceptionMapper());
        singletons.add(new InvalidArgumentExceptionMapper());
        singletons.add(new LockExceptionMapper());
        singletons.add(new ItemNotFoundExceptionMapper());
        singletons.add(new ItemAlreadyExistExceptionMapper());
        singletons.add(new NotSupportedExceptionMapper());
        singletons.add(new PermissionDeniedExceptionMapper());
        singletons.add(new LocalPathResolveExceptionMapper());
        singletons.add(new GitUrlResolveExceptionMapper());
    }

    /** @see javax.ws.rs.core.Application#getClasses() */
    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    /** @see javax.ws.rs.core.Application#getSingletons() */
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
