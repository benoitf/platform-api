/*
 * Copyright (C) 2011 eXo Platform SAS.
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
package org.exoplatform.ide.vfs.client;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import org.exoplatform.ide.vfs.shared.AccessControlEntry;
import org.exoplatform.ide.vfs.shared.Property;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id: JSONSerializer.java 75889 2011-11-01 10:42:51Z anya $
 */
public abstract class JSONSerializer<O>
{
   // --------- Common serializers. -------------
   public static final JSONSerializer<String> STRING_SERIALIZER = new JSONSerializer<String>()
   {
      @Override
      public JSONValue fromObject(String object)
      {
         if (object == null)
         {
            return JSONNull.getInstance();
         }
         return new JSONString(object);
      }
   };

   // --------- Customized serializers. -------------
   @SuppressWarnings({"unchecked", "rawtypes"})
   public static final JSONSerializer<Property> PROPERTY_SERIALIZER = new JSONSerializer<Property>()
   {
      @Override
      public JSONValue fromObject(Property source)
      {
         if (source == null)
         {
            return JSONNull.getInstance();
         }
         JSONObject target = new JSONObject();
         target.put("name", STRING_SERIALIZER.fromObject(source.getName()));
         target.put("value", STRING_SERIALIZER.fromCollection(source.getValue()));
         return target;
      }
   };

   public static final JSONSerializer<AccessControlEntry> ACL_SERIALIZER = new JSONSerializer<AccessControlEntry>()
   {
      @Override
      public JSONValue fromObject(AccessControlEntry source)
      {
         if (source == null)
         {
            return JSONNull.getInstance();
         }
         JSONObject target = new JSONObject();
         target.put("principal", STRING_SERIALIZER.fromObject(source.getPrincipal()));
         target.put("permissions", STRING_SERIALIZER.fromCollection(source.getPermissions()));
         return target;
      }
   };

   public JSONValue fromArray(O[] source)
   {
      if (source == null)
      {
         return JSONNull.getInstance();
      }
      JSONArray target = new JSONArray();
      for (int i = 0; i < source.length; i++)
      {
         target.set(i, fromObject(source[i]));
      }
      return target;
   }

   public JSONValue fromCollection(Collection<O> source)
   {
      if (source == null)
      {
         return JSONNull.getInstance();
      }
      JSONArray target = new JSONArray();
      int idx = 0;
      for (Iterator<O> i = source.iterator(); i.hasNext(); )
      {
         target.set(idx++, fromObject(i.next()));
      }
      return target;
   }

   public JSONValue fromMap(Map<String, O> source)
   {
      if (source == null)
      {
         return JSONNull.getInstance();
      }
      JSONObject target = new JSONObject();
      for (Iterator<Map.Entry<String, O>> i = source.entrySet().iterator(); i.hasNext(); )
      {
         Map.Entry<String, O> next = i.next();
         target.put(next.getKey(), fromObject(next.getValue()));
      }
      return target;
   }

   public abstract JSONValue fromObject(O source);
}
