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
package org.exoplatform.ide.vfs.client.event;

import com.google.gwt.event.shared.GwtEvent;

import org.exoplatform.ide.vfs.client.model.FolderModel;

/**
 * Created by The eXo Platform SAS.
 *
 * @author <a href="mailto:vitaly.parfonov@gmail.com">Vitaly Parfonov</a>
 * @version $Id: $
 */
public class SearchResultReceivedEvent extends GwtEvent<SearchResultReceivedHandler> {

    public static final GwtEvent.Type<SearchResultReceivedHandler> TYPE = new Type<SearchResultReceivedHandler>();

    private FolderModel folder;

    public SearchResultReceivedEvent(FolderModel folder) {
        this.folder = folder;
    }

    @Override
    protected void dispatch(SearchResultReceivedHandler handler) {
        handler.onSearchResultReceived(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<SearchResultReceivedHandler> getAssociatedType() {
        return TYPE;
    }

    public FolderModel getFolder() {
        return folder;
    }

}
