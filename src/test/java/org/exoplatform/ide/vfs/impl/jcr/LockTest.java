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

import org.exoplatform.services.jcr.access.PermissionType;
import org.exoplatform.services.jcr.core.ExtendedNode;
import org.exoplatform.services.rest.impl.ContainerResponse;
import org.exoplatform.services.rest.tools.ByteArrayContainerResponseWriter;

import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;

/**
 * @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id$
 */
public class LockTest extends JcrFileSystemTest
{
   private Node lockTestNode;

   private String folder;

   private String document;

   /**
    * @see org.exoplatform.ide.vfs.impl.jcr.JcrFileSystemTest#setUp()
    */
   @Override
   protected void setUp() throws Exception
   {
      super.setUp();
      String name = getClass().getName();
      lockTestNode = testRoot.addNode(name, "nt:unstructured");
      lockTestNode.addMixin("exo:privilegeable");

      Node folderNode = lockTestNode.addNode("LockTest_FOLDER", "nt:folder");
      folder = folderNode.getPath();

      Node documentNode = lockTestNode.addNode("LockTest_DOCUMENT", "nt:file");
      Node contentNode = documentNode.addNode("jcr:content", "nt:resource");
      contentNode.setProperty("jcr:mimeType", "text/plain");
      contentNode.setProperty("jcr:lastModified", Calendar.getInstance());
      contentNode.setProperty("jcr:data", new ByteArrayInputStream(DEFAULT_CONTENT.getBytes()));
      document = documentNode.getPath();

      session.save();
   }

   public void testLockDocument() throws Exception
   {
      ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
      String path = new StringBuilder() //
         .append("/vfs/jcr/db1/ws/lock") //
         .append(document).toString();
      ContainerResponse response = launcher.service("POST", path, "", null, null, writer, null);
      assertEquals(200, response.getStatus());
      log.info(new String(writer.getBody()));
      Node doc = (Node)session.getItem(document);
      assertTrue("Document must be locked. ", doc.isLocked());
   }

   public void testLockDocumentAlreadyLocked() throws Exception
   {
      Node node = ((Node)session.getItem(document));
      node.addMixin("mix:lockable");
      session.save();
      node.lock(true, false);
      
      ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
      String path = new StringBuilder() //
         .append("/vfs/jcr/db1/ws/lock") //
         .append(document).toString();
      ContainerResponse response = launcher.service("POST", path, "", null, null, writer, null);
      assertEquals(423, response.getStatus());
      log.info(new String(writer.getBody()));
   }

   public void testLockDocumentNoPermissions() throws Exception
   {
      Map<String, String[]> permissions = new HashMap<String, String[]>(1);
      permissions.put("root", PermissionType.ALL);
      ((ExtendedNode)lockTestNode).setPermissions(permissions);
      session.save();
      
      ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
      String path = new StringBuilder() //
         .append("/vfs/jcr/db1/ws/lock") //
         .append(document).toString();
      ContainerResponse response = launcher.service("POST", path, "", null, null, writer, null);
      assertEquals(403, response.getStatus());
      log.info(new String(writer.getBody()));
   }

   public void testLockFolder() throws Exception
   {
      ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
      String path = new StringBuilder() //
         .append("/vfs/jcr/db1/ws/lock") //
         .append(folder).toString();
      ContainerResponse response = launcher.service("POST", path, "", null, null, writer, null);
      assertEquals(200, response.getStatus());
      log.info(new String(writer.getBody()));
      Node folder = (Node)session.getItem(this.folder);
      assertTrue("Folder must be locked. ", folder.isLocked());
   }
}
