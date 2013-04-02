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
package org.exoplatform.ide.vfs.server.impl.memory;

import org.everrest.core.impl.ContainerResponse;
import org.everrest.core.tools.ByteArrayContainerResponseWriter;
import org.exoplatform.ide.vfs.server.impl.memory.context.MemoryFile;
import org.exoplatform.ide.vfs.server.impl.memory.context.MemoryFolder;
import org.exoplatform.ide.vfs.shared.AccessControlEntry;
import org.exoplatform.ide.vfs.shared.AccessControlEntryImpl;
import org.exoplatform.ide.vfs.shared.ExitCodes;
import org.exoplatform.ide.vfs.shared.VirtualFileSystemInfoImpl;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashSet;

/**
 * @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id: MoveTest.java 75032 2011-10-13 15:24:34Z andrew00x $
 */
public class MoveTest extends MemoryFileSystemTest {
    private MemoryFolder moveTestDestinationFolder;
    private MemoryFolder moveTestDestinationProject;
    private MemoryFolder folderForMove;
    private MemoryFile   fileForMove;
    private MemoryFolder projectForMove;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        String name = getClass().getName();
        MemoryFolder moveTestFolder = new MemoryFolder(name);
        testRoot.addChild(moveTestFolder);

        moveTestDestinationFolder = new MemoryFolder(name + "_MoveTest_DESTINATION_FOLDER");
        testRoot.addChild(moveTestDestinationFolder);

        moveTestDestinationProject = new MemoryFolder(name + "_MoveTest_DESTINATION_PROJECT");
        moveTestDestinationProject.setMediaType("text/vnd.ideproject+directory");
        assertTrue(moveTestDestinationProject.isProject());
        testRoot.addChild(moveTestDestinationProject);

        folderForMove = new MemoryFolder("MoveTest_FOLDER");
        moveTestFolder.addChild(folderForMove);
        // add child in folder
        MemoryFile childFile = new MemoryFile("file", "text/plain",
                                              new ByteArrayInputStream(DEFAULT_CONTENT.getBytes()));
        folderForMove.addChild(childFile);

        fileForMove = new MemoryFile("MoveTest_FILE", "text/plain",
                                     new ByteArrayInputStream(DEFAULT_CONTENT.getBytes()));
        moveTestFolder.addChild(fileForMove);

        projectForMove = new MemoryFolder("MoveTest_PROJECT");
        projectForMove.setMediaType("text/vnd.ideproject+directory");
        assertTrue(projectForMove.isProject());
        moveTestFolder.addChild(projectForMove);

        memoryContext.putItem(moveTestFolder);
        memoryContext.putItem(moveTestDestinationFolder);
        memoryContext.putItem(moveTestDestinationProject);
    }

    public void testMoveFile() throws Exception {
        String path = SERVICE_URI + "move/" + fileForMove.getId() + '?' + "parentId=" + moveTestDestinationFolder.getId();
        String originPath = fileForMove.getPath();
        ContainerResponse response = launcher.service("POST", path, BASE_URI, null, null, null);
        assertEquals(200, response.getStatus());
        String expectedPath = moveTestDestinationFolder.getPath() + '/' + fileForMove.getName();
        assertNull("File must be moved. ", memoryContext.getItemByPath(originPath));
        assertNotNull("Not found file in destination location. ", memoryContext.getItemByPath(expectedPath));
    }

    public void testMoveFileAlreadyExist() throws Exception {
        MemoryFile existed = new MemoryFile(fileForMove.getName(), "text/plain",
                                            new ByteArrayInputStream(DEFAULT_CONTENT.getBytes()));
        moveTestDestinationFolder.addChild(existed);
        memoryContext.putItem(existed);
        String path = SERVICE_URI + "move/" + fileForMove.getId() + '?' + "parentId=" + moveTestDestinationFolder.getId();
        ContainerResponse response = launcher.service("POST", path, BASE_URI, null, null, null);
        assertEquals(400, response.getStatus());
        assertEquals(ExitCodes.ITEM_EXISTS, Integer.parseInt((String)response.getHttpHeaders().getFirst("X-Exit-Code")));
    }

    public void testMoveLockedFile() throws Exception {
        String lockToken = fileForMove.lock();
        String path = SERVICE_URI + "move/" + fileForMove.getId() + '?' + "parentId=" + moveTestDestinationFolder.getId() +
                      '&' + "lockToken=" + lockToken;
        String originPath = fileForMove.getPath();
        ContainerResponse response = launcher.service("POST", path, BASE_URI, null, null, null);
        assertEquals(200, response.getStatus());
        String expectedPath = moveTestDestinationFolder.getPath() + '/' + fileForMove.getName();
        assertNull("File must be moved. ", memoryContext.getItemByPath(originPath));
        assertNotNull("Not found file in destination location. ", memoryContext.getItemByPath(expectedPath));
    }

    public void testMoveLockedFile_NoLockToken() throws Exception {
        fileForMove.lock();
        ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
        String path = SERVICE_URI + "move/" + fileForMove.getId() + '?' + "parentId=" + moveTestDestinationFolder.getId();
        String originPath = fileForMove.getPath();
        ContainerResponse response = launcher.service("POST", path, BASE_URI, null, null, writer, null);
        log.info(new String(writer.getBody()));
        assertEquals(423, response.getStatus());
        assertNotNull("File must not be moved since it is locked. ", memoryContext.getItemByPath(originPath));
        String expectedPath = moveTestDestinationFolder.getPath() + '/' + fileForMove.getName();
        assertNull("File must not be moved since it is locked.", memoryContext.getItemByPath(expectedPath));
    }

    public void testMoveFileNoPermissions() throws Exception {
        AccessControlEntry ace = new AccessControlEntryImpl();
        ace.setPrincipal("admin");
        ace.setPermissions(new HashSet<String>(Arrays.asList(VirtualFileSystemInfoImpl.BasicPermissions.ALL.value())));
        fileForMove.updateACL(Arrays.asList(ace), true);

        ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
        String path = SERVICE_URI + "move/" + fileForMove.getId() + '?' + "parentId=" + moveTestDestinationFolder.getId();
        String originPath = fileForMove.getPath();
        ContainerResponse response = launcher.service("POST", path, BASE_URI, null, null, writer, null);
        log.info(new String(writer.getBody()));
        assertEquals(403, response.getStatus());
        assertNotNull("File must not be moved since permissions restriction. ", memoryContext.getItemByPath(originPath));
        String expectedPath = moveTestDestinationFolder.getPath() + '/' + fileForMove.getName();
        assertNull("File must not be moved since permissions restriction.", memoryContext.getItemByPath(expectedPath));
    }

    public void testMoveFileDestination_NoPermissions() throws Exception {
        AccessControlEntry adminACE = new AccessControlEntryImpl();
        adminACE.setPrincipal("admin");
        adminACE.setPermissions(new HashSet<String>(Arrays.asList(VirtualFileSystemInfoImpl.BasicPermissions.ALL.value())));
        AccessControlEntry userACE = new AccessControlEntryImpl();
        userACE.setPrincipal("john");
        userACE.setPermissions(new HashSet<String>(Arrays.asList(VirtualFileSystemInfoImpl.BasicPermissions.READ.value())));
        moveTestDestinationFolder.updateACL(Arrays.asList(adminACE, userACE), true);

        ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
        String path = SERVICE_URI + "move/" + fileForMove.getId() + '?' + "parentId=" + moveTestDestinationFolder.getId();
        String originPath = fileForMove.getPath();
        ContainerResponse response = launcher.service("POST", path, BASE_URI, null, null, writer, null);
        log.info(new String(writer.getBody()));
        assertEquals(403, response.getStatus());
        assertNotNull("Source file not found. ", memoryContext.getItemByPath(originPath));
        String expectedPath = moveTestDestinationFolder.getPath() + '/' + fileForMove.getName();
        assertNull("File must not be moved since permissions restriction on destination folder. ",
                   memoryContext.getItemByPath(expectedPath));
    }

    public void testMoveFolder() throws Exception {
        String path = SERVICE_URI + "move/" + folderForMove.getId() + '?' + "parentId=" + moveTestDestinationFolder.getId();
        String originPath = folderForMove.getPath();
        ContainerResponse response = launcher.service("POST", path, BASE_URI, null, null, null);
        assertEquals(200, response.getStatus());
        String expectedPath = moveTestDestinationFolder.getPath() + '/' + folderForMove.getName();
        assertNull("Folder must be moved. ", memoryContext.getItemByPath(originPath));
        assertNotNull("Not found folder in destination location. ", memoryContext.getItemByPath(expectedPath));
        assertNotNull("Child of folder missing after moving. ", memoryContext.getItemByPath(expectedPath + "/file"));
    }

    public void testMoveFolderAlreadyExist() throws Exception {
        MemoryFolder existed = new MemoryFolder(folderForMove.getName());
        moveTestDestinationFolder.addChild(existed);
        memoryContext.putItem(existed);

        String path = SERVICE_URI + "move/" + folderForMove.getId() + '?' + "parentId=" + moveTestDestinationFolder.getId();
        ContainerResponse response = launcher.service("POST", path, BASE_URI, null, null, null);
        assertEquals(400, response.getStatus());
        assertEquals(ExitCodes.ITEM_EXISTS, Integer.parseInt((String)response.getHttpHeaders().getFirst("X-Exit-Code")));
    }

    public void testMoveProjectToProject() throws Exception {
        String path = SERVICE_URI + "move/" + projectForMove.getId() + '?' + "parentId=" + moveTestDestinationProject.getId();
        final String originPath = projectForMove.getPath();
        ContainerResponse response = launcher.service("POST", path, BASE_URI, null, null, null);
        log.info(response.getEntity());
        assertEquals("Unexpected status " + response.getStatus(), 200, response.getStatus());
        String expectedPath = moveTestDestinationProject.getPath() + '/' + projectForMove.getName();
        assertNull("Project must be moved. ", memoryContext.getItemByPath(originPath));
        assertNotNull("Not found project in destination location. ", memoryContext.getItemByPath(expectedPath));
    }
}
