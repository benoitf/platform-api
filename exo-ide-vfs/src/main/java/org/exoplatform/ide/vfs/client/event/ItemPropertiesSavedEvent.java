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

import org.exoplatform.ide.vfs.shared.Item;

/**
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 * @version $Id: Aug 31, 2011 evgen $
 */
public class ItemPropertiesSavedEvent extends GwtEvent<ItemPropertiesSavedHandler> {

    public static final GwtEvent.Type<ItemPropertiesSavedHandler> TYPE = new Type<ItemPropertiesSavedHandler>();

    private Item item;

    /** @param item */
    public ItemPropertiesSavedEvent(Item item) {
        super();
        this.item = item;
    }

    /** @see com.google.gwt.event.shared.GwtEvent#getAssociatedType() */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<ItemPropertiesSavedHandler> getAssociatedType() {
        return TYPE;
    }

    /** @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler) */
    @Override
    protected void dispatch(ItemPropertiesSavedHandler handler) {
        handler.onItemPropertiesSaved(this);
    }

    /** @return the item */
    public Item getItem() {
        return item;
    }

}
