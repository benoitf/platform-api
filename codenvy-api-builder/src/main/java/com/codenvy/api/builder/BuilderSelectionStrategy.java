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
package com.codenvy.api.builder;

import java.util.List;

/**
 * Selects the 'best' SlaveBuilder from the List according to implementation. BuildQueue uses implementation of this interface fo
 * find the 'best' slave-builder for processing incoming build request. If more then one slave-builder available then BuildQueue collects
 * them (their front-ends which are represented by SlaveBuilder) and passes to implementation of this interface. This implementation
 * should select the 'best' one.
 *
 * @author andrew00x
 */
public interface BuilderSelectionStrategy {
    RemoteBuilder select(List<RemoteBuilder> slaveBuilders);
}
