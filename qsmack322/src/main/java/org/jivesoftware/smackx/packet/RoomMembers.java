package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public class RoomMembers extends IQ {

	private List<Item> members;
	private String node;

	/**
	 * Adds a new item to the discovered information.
	 * 
	 * @param item the discovered entity's item
	 */
	public void addItem(Item item) {
		synchronized (members) {
			members.add(item);
		}
	}

	/**
	 * Returns the discovered items of the queried XMPP entity. 
	 *
	 * @return an Iterator on the discovered entity's items
	 */
	public Iterator<RoomMembers.Item> getItems() {
		synchronized (members) {
			return Collections.unmodifiableList(members).iterator();
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

	public RoomMembers()
	{
		this.members = new ArrayList<RoomMembers.Item>();
	}

	@Override
	public String getChildElementXML()
	{
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"http://jabber.org/protocol/muc#members\"");
        if (getNode() != null) {
            buf.append(" node=\"");
            buf.append(getNode());
            buf.append("\"");
        }
        buf.append(">");
                
		synchronized (members) {
			for (Item room : members) {
				buf.append(room.toXml());
			}
		}
		buf.append("</query>");
		return buf.toString();
	}

	public static class Item {
		public String _jid;
		public String _affiliation;
		public String _firstname;
		public String _lastname;
		public String _username;
		public String _url1, _url2;
		
		public Item(String jid, String affiliation, String firstname, String lastname,
				String username, String url1, String url2) {
			this._jid = jid;
			this._affiliation = affiliation;
			this._firstname = firstname;
			this._lastname = lastname;
			this._username = username;
			this._url1 = url1;
			this._url2 = url2;
		}

		public String toXml() {
			StringBuilder builder = new StringBuilder("<subscriber jid=\"");
			builder.append(_jid).append("\" ");
			builder.append("affiliation=\"").append(_affiliation).append("\" ");
			builder.append("firstname=\"").append(_firstname).append("\" ");
			builder.append("lastname=\"").append(_lastname).append("\" ");
			builder.append("username=\"").append(_username).append("\" ");
			builder.append("url1=\"").append(_url1).append("\" ");
			builder.append("url2=\"").append(_url2).append("\"/>");
			return builder.toString();
		}
	}
}
