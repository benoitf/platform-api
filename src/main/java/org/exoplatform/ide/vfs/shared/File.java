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
package org.exoplatform.ide.vfs.shared;

import java.util.List;
import java.util.Map;

/**
 * Representation of File object used to interaction with client via JSON.
 *
 * @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id$
 */
public class File extends Item
{
   /** Id of version of file. */
   protected String versionId;

   /** Content length. */
   protected long length = -1;

   /** Date of last modification in long format. */
   protected long lastModificationDate;

   /** Locking flag. */
   protected boolean locked;

   /**
    * Instance of file with specified attributes.
    *
    * @param id id of object
    * @param name the name of object
    * @param path path of object
    * @param parentId id of parent folder
    * @param creationDate creation date in long format
    * @param lastModificationDate date of last modification in long format
    * @param versionId id of versions of file
    * @param mimeType media type of content
    * @param length content length
    * @param locked is file locked or not
    * @param properties other properties of file
    * @param links hyperlinks for retrieved or(and) manage item
    */
   @SuppressWarnings({"rawtypes", "unchecked"})
   public File(String id, String name, String path, String parentId, long creationDate, long lastModificationDate,
               String versionId, String mimeType, long length, boolean locked, List properties, Map<String, Link> links)
   {
      super(id, name, ItemType.FILE, mimeType, path, parentId, creationDate, properties, links);
      this.lastModificationDate = lastModificationDate;
      this.locked = locked;
      this.versionId = versionId;
      this.length = length;
   }

   /** Empty instance of file. */
   public File()
   {
      super(ItemType.FILE);
   }

   /** @return version id */
   public String getVersionId()
   {
      return versionId;
   }

   /** @param versionId the version id */
   public void setVersionId(String versionId)
   {
      this.versionId = versionId;
   }

   /** @return content length */
   public long getLength()
   {
      return length;
   }

   /** @param length the content length */
   public void setLength(long length)
   {
      this.length = length;
   }

   /** @return date of last modification */
   public long getLastModificationDate()
   {
      return lastModificationDate;
   }

   /** @param lastModificationDate the date of last modification */
   public void setLastModificationDate(long lastModificationDate)
   {
      this.lastModificationDate = lastModificationDate;
   }

   /** @return <code>true</code> if object locked and <code>false</code> otherwise */
   public boolean isLocked()
   {
      return locked;
   }

   /** @param locked locking flag. Must be <code>true</code> if object locked and <code>false</code> otherwise */
   public void setLocked(boolean locked)
   {
      this.locked = locked;
   }
}
