/**
 * $RCSfile$
 * $Revision: 9548 $
 * $Date: 2007-12-02 18:00:03 -0600 (Sun, 02 Dec 2007) $
 *
 * Copyright 2003-2007 Jive Software.
 *
 * All rights reserved. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.util.StringUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Represents XMPP roster packets.
 *
 * @author Matt Tucker
 */
public class PublicNotificationPacket extends IQ {

    private List<Item> rosterItems = new ArrayList<Item>();
    
        
    public PublicNotificationPacket() {    
    }

    /**
     * Adds a roster item to the packet.
     *
     * @param item a roster item.
     */
    public void addRosterItem(Item item) {
        synchronized (rosterItems) {
            rosterItems.add(item);
        }
    }

    /**
     * Returns the number of roster items in this roster packet.
     *
     * @return the number of roster items.
     */
    public int getRosterItemCount() {
        synchronized (rosterItems) {
            return rosterItems.size();
        }
    }

    /**
     * Returns an unmodifiable collection for the roster items in the packet.
     *
     * @return an unmodifiable collection for the roster items in the packet.
     */
    public Collection<Item> getRosterItems() {
        synchronized (rosterItems) {
            return Collections.unmodifiableList(new ArrayList<Item>(rosterItems));
        }
    }

    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"jabber:iq:publicperm:roster\">");
        
        synchronized (rosterItems) {
            for (Item entry : rosterItems) {
                buf.append(entry.toXML());
            }
        }
        buf.append("</query>");
        return buf.toString();
    }

    /**
     * A roster item, which consists of a JID, their name, the type of subscription, and
     * the groups the roster item belongs to.
     */
    public static class Item {

    	public String userJid;
    	public String username;
    	public String firstname;
    	public String lastname;
    	public String lastContacted;
    	public String url1;
    	public String url2;
    	public String notifyPublicCall;
    	public String notifyAppOpened;
    	public String notifyAppMinimized;

        /**
         * Creates a new roster item.
         *
         * @param user the user.
         * @param name the user's name.
         */
        public Item() {
            
        }
        
		public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<item jid=\"").append(userJid).append("\"");
            if (username != null) {
                buf.append(" name=\"").append(StringUtils.escapeForXML(username)).append("\"");
            }
            // custom oncam
            if (firstname != null) {
                buf.append(" firstname=\"").append(StringUtils.escapeForXML(firstname)).append("\"");
            }
            if (lastname != null) {
                buf.append(" lastname=\"").append(StringUtils.escapeForXML(lastname)).append("\"");
            }
            if (url1 != null) {
                buf.append(" url1=\"").append(url1).append("\"");
            }//jitendra
            if (url2 != null) {
                buf.append(" url2=\"").append(url2).append("\"");
            }
            if (lastContacted != null) {
                buf.append(" contacted=\"").append(lastContacted).append("\"");
            }

            //custom oncam
            if (notifyAppMinimized != null) {
                buf.append(" p_pub_app_min=\"").append(notifyAppMinimized).append("\"");
            }
            
            if (notifyAppOpened != null) {
                buf.append(" p_pub_app_open=\"").append(notifyAppOpened).append("\"");
            }
            if (notifyPublicCall != null) {
                buf.append(" p_pub_call=\"").append(notifyPublicCall).append("\"");
            }
            buf.append("/>");            
            return buf.toString();
        }
    }    
}
