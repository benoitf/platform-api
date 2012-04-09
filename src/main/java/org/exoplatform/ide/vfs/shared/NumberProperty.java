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
package org.exoplatform.ide.vfs.shared;

import java.util.List;

/**
 * @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id: NumberProperty.java 65787 2011-02-03 10:03:58Z andrew00x $
 */
public class NumberProperty extends Property<Double>
{
   public NumberProperty()
   {
      super();
   }

   public NumberProperty(String name, Double value)
   {
      super(name, value);
   }

   public NumberProperty(String name, List<Double> value)
   {
      super(name, value);
   }

   public List<Double> getValue()
   {
      return value;
   }

   public void setValue(List<Double> value)
   {
      this.value = value;
   }
}
