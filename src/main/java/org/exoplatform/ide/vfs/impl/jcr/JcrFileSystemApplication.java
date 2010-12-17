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
package org.exoplatform.ide.vfs.impl.jcr;

import org.exoplatform.ide.vfs.server.exceptions.ConstraintExceptionMapper;
import org.exoplatform.ide.vfs.server.exceptions.InvalidArgumentExceptionMapper;
import org.exoplatform.ide.vfs.server.exceptions.LockExceptionMapper;
import org.exoplatform.ide.vfs.server.exceptions.NotSupportedExceptionMapper;
import org.exoplatform.ide.vfs.server.exceptions.ObjectNotFoundExceptionMapper;
import org.exoplatform.ide.vfs.server.exceptions.PermissionDeniedExceptionMapper;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.ext.app.ThreadLocalSessionProviderService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id$
 */
public class JcrFileSystemApplication extends Application
{
   private final Set<Object> singletons = new HashSet<Object>();

   public JcrFileSystemApplication(RepositoryService repositoryService, ThreadLocalSessionProviderService sessionFactory)
   {
      singletons.add(new JcrFileSystemFactory(repositoryService, sessionFactory));
      singletons.add(new ConstraintExceptionMapper());
      singletons.add(new InvalidArgumentExceptionMapper());
      singletons.add(new LockExceptionMapper());
      singletons.add(new ObjectNotFoundExceptionMapper());
      singletons.add(new NotSupportedExceptionMapper());
      singletons.add(new PermissionDeniedExceptionMapper());
   }

   /**
    * @see javax.ws.rs.core.Application#getClasses()
    */
   @Override
   public Set<Class<?>> getClasses()
   {
      return Collections.emptySet();
   }

   /**
    * @see javax.ws.rs.core.Application#getSingletons()
    */
   @Override
   public Set<Object> getSingletons()
   {
      return singletons;
   }

}
