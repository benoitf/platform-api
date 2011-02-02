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

import com.google.gwt.json.client.JSONNumber;

/**
 * @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id$
 */
public class ExtJSONNumber extends JSONNumber
{
   public ExtJSONNumber(double value)
   {
      super(value);
   }

   public byte byteValue()
   {
      return (byte)doubleValue();
   }

   public short shortValue()
   {
      return (short)doubleValue();
   }

   public int intValue()
   {
      return (int)doubleValue();
   }

   public long longValue()
   {
      return (long)doubleValue();
   }

   public float floatValue()
   {
      return (float)doubleValue();
   }
}
