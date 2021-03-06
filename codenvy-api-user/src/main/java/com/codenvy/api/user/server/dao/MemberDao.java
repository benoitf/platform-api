/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [2013] Codenvy, S.A.
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
package com.codenvy.api.user.server.dao;


import com.codenvy.api.user.server.exception.MembershipException;
import com.codenvy.api.user.shared.dto.Member;

import java.util.List;

/**
 * DAO interface offers means to perform CRUD operations with {@link com.codenvy.api.user.shared.dto.Member} data.
 * The implementation is not required
 * to be responsible for persistent layer data dto consistency. It simply transfers data from one layer to another,
 * so
 * if you're going to call any of implemented methods it is considered that all needed verifications are already done.
 * <p> <strong>Note:</strong> This particularly does not mean that method call will not make any inconsistency but this
 * mean that such kind of inconsistencies are expected by design and may be treated further. </p>
 */
public interface MemberDao {

    /**
     * Adds a new Member to persistent layer.
     *
     * @param member
     *         - POJO representation of workspace member
     * @throws com.codenvy.api.user.server.exception.MembershipException
     */

    void create(Member member) throws MembershipException;


    /**
     * Updates member in persistent layer.
     *
     * @param member
     *         - POJO representation of workspace member
     * @throws com.codenvy.api.user.server.exception.MembershipException
     */
    void update(Member member) throws MembershipException;

    /**
     * Gets a list of all members of the given workspace.
     *
     * @param wsId
     *         workspace to search in
     * @return list of workspace members
     * @throws com.codenvy.api.user.server.exception.MembershipException
     */

    List<Member> getWorkspaceMembers(String wsId) throws MembershipException;


    /**
     * Gets a list of all relationships of the given user and workspaces.
     *
     * @param userId
     *         user to get relationships
     * @return list of user relations
     * @throws com.codenvy.api.user.server.exception.MembershipException
     */

    public List<Member> getUserRelationships(String userId) throws MembershipException;

    /**
     * Removes a given member from specified workspace.
     *
     * @param member
     *         member to remove
     * @throws com.codenvy.api.user.server.exception.MembershipException
     */

    void remove(Member member) throws MembershipException;
}
