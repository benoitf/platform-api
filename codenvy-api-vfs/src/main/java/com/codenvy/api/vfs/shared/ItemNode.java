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
package com.codenvy.api.vfs.shared;

import java.util.List;

/** @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a> */
public interface ItemNode {
    Item getItem();

    void setItem(Item item);

    /**
     * Get children of item.
     *
     * @return children of item. Always return <code>null</code> for files
     */
    List<ItemNode> getChildren();

    /**
     * Set children of item.
     *
     * @param children
     *         of item. Always must be <code>null</code> for files
     */
    void setChildren(List<ItemNode> children);
}