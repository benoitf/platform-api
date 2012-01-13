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
package org.exoplatform.ide.vfs.client.model;

import org.exoplatform.ide.vfs.shared.Item;

/**
 * Wrapper for {@link ItemWrapper} to help unmarshall item unknown type
 * 
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 * @version $Id: Sep 29, 2011 evgen $
 * 
 */
public class ItemWrapper
{
   private Item item;

   /**
    * 
    */
   public ItemWrapper()
   {
   }

   /**
    * @param item
    */
   public ItemWrapper(Item item)
   {
      this.item = item;
   }

   /**
    * @return the item
    */
   public Item getItem()
   {
      return item;
   }

   /**
    * @param item the item to set
    */
   public void setItem(Item item)
   {
      this.item = item;
   }

}
