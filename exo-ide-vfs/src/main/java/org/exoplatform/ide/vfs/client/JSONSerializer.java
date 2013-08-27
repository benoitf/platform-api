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
package org.exoplatform.ide.vfs.client;

import com.google.gwt.json.client.*;

import org.exoplatform.ide.vfs.shared.AccessControlEntry;
import org.exoplatform.ide.vfs.shared.Principal;
import org.exoplatform.ide.vfs.shared.Property;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id: JSONSerializer.java 75889 2011-11-01 10:42:51Z anya $
 */
public abstract class JSONSerializer<O> {
    // --------- Common serializers. -------------
    public static final JSONSerializer<String> STRING_SERIALIZER = new JSONSerializer<String>() {
        @Override
        public JSONValue fromObject(String object) {
            if (object == null) {
                return JSONNull.getInstance();
            }
            return new JSONString(object);
        }
    };

    // --------- Customized serializers. -------------
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final JSONSerializer<Property> PROPERTY_SERIALIZER = new JSONSerializer<Property>() {
        @Override
        public JSONValue fromObject(Property source) {
            if (source == null) {
                return JSONNull.getInstance();
            }
            JSONObject target = new JSONObject();
            target.put("name", STRING_SERIALIZER.fromObject(source.getName()));
            target.put("value", STRING_SERIALIZER.fromCollection(source.getValue()));
            return target;
        }
    };

    public static final JSONSerializer<AccessControlEntry> ACL_SERIALIZER = new JSONSerializer<AccessControlEntry>() {
        @Override
        public JSONValue fromObject(AccessControlEntry source) {
            if (source == null) {
                return JSONNull.getInstance();
            }
            JSONObject target = new JSONObject();
            target.put("principal", PRINCIPAL_SERIALIZER.fromObject(source.getPrincipal()));
            target.put("permissions", STRING_SERIALIZER.fromCollection(source.getPermissions()));
            return target;
        }
    };

    public static final JSONSerializer<Principal> PRINCIPAL_SERIALIZER = new JSONSerializer<Principal>() {
        @Override
        public JSONValue fromObject(Principal source) {
            if (source == null) {
                return JSONNull.getInstance();
            }
            JSONObject target = new JSONObject();
            target.put("name", STRING_SERIALIZER.fromObject(source.getName()));
            target.put("type", STRING_SERIALIZER.fromObject(source.getType().toString()));
            return target;
        }
    };

    public JSONValue fromArray(O[] source) {
        if (source == null) {
            return JSONNull.getInstance();
        }
        JSONArray target = new JSONArray();
        for (int i = 0; i < source.length; i++) {
            target.set(i, fromObject(source[i]));
        }
        return target;
    }

    public JSONValue fromCollection(Collection<O> source) {
        if (source == null) {
            return JSONNull.getInstance();
        }
        JSONArray target = new JSONArray();
        int idx = 0;
        for (Iterator<O> i = source.iterator(); i.hasNext(); ) {
            target.set(idx++, fromObject(i.next()));
        }
        return target;
    }

    public JSONValue fromMap(Map<String, O> source) {
        if (source == null) {
            return JSONNull.getInstance();
        }
        JSONObject target = new JSONObject();
        for (Iterator<Map.Entry<String, O>> i = source.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<String, O> next = i.next();
            target.put(next.getKey(), fromObject(next.getValue()));
        }
        return target;
    }

    public abstract JSONValue fromObject(O source);
}
