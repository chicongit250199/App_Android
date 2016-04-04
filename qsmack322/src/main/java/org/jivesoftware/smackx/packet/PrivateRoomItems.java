package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;

public class PrivateRoomItems extends IQ {

	private List<Item> rooms;
	private String node;

	/**
	 * Adds a new item to the discovered information.
	 * 
	 * @param item the discovered entity's item
	 */
	public void addItem(Item item) {
		synchronized (rooms) {
			rooms.add(item);
		}
	}

	/**
	 * Returns the discovered items of the queried XMPP entity. 
	 *
	 * @return an Iterator on the discovered entity's items
	 */
	public Iterator<PrivateRoomItems.Item> getItems() {
		synchronized (rooms) {
			return Collections.unmodifiableList(rooms).iterator();
		}
	}

	/**
	 * Returns the node attribute that supplements the 'jid' attribute. A node is merely 
	 * something that is associated with a JID and for which the JID can provide information.<p> 
	 * 
	 * Node attributes SHOULD be used only when trying to provide or query information which 
	 * is not directly addressable.
	 *
	 * @return the node attribute that supplements the 'jid' attribute
	 */
	public String getNode() {
		return node;
	}

	/**
	 * Sets the node attribute that supplements the 'jid' attribute. A node is merely 
	 * something that is associated with a JID and for which the JID can provide information.<p> 
	 * 
	 * Node attributes SHOULD be used only when trying to provide or query information which 
	 * is not directly addressable.
	 * 
	 * @param node the node attribute that supplements the 'jid' attribute
	 */
	public void setNode(String node) {
		this.node = node;
	}

	public PrivateRoomItems()
	{
		this.rooms = new ArrayList<PrivateRoomItems.Item>();
	}

	@Override
	public String getChildElementXML()
	{
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"http://jabber.org/protocol/muc#rooms\"");
        if (getNode() != null) {
            buf.append(" node=\"");
            buf.append(getNode());
            buf.append("\"");
        }
        buf.append(">");
                
		synchronized (rooms) {
			for (Item room : rooms) {
				buf.append(room.toXml());
			}
		}
		buf.append("</query>");
		return buf.toString();
	}

	public static class Item {
		public String _name;
		public String _subject;
		public String _avatar;
		public String _admin;

		public String toXml() {
			StringBuilder builder = new StringBuilder();
			builder.append("<room name=\"").append(_name);
			builder.append("\" subject=\"").append(_subject);
			builder.append("\" admin=\"").append(_admin);
			builder.append("\" avatar=\"").append(_avatar).append("\"/>");
			return builder.toString();
		}
	}
}
