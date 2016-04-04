package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.packet.DiscoverItems.Item;

public class RoomHistory extends IQ {

	private List<Item> histories;
	private String node;

	/**
	 * Adds a new item to the discovered information.
	 * 
	 * @param item the discovered entity's item
	 */
	public void addItem(Item item) {
		synchronized (histories) {
			histories.add(item);
		}
	}

	/**
	 * Returns the discovered items of the queried XMPP entity. 
	 *
	 * @return an Iterator on the discovered entity's items
	 */
	public Iterator<RoomHistory.Item> getItems() {
		synchronized (histories) {
			return Collections.unmodifiableList(histories).iterator();
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

	public RoomHistory()
	{
		this.histories = new ArrayList<RoomHistory.Item>();
	}

	@Override
	public String getChildElementXML()
	{
		StringBuilder buf = new StringBuilder();
        buf.append("<command xmlns=\"http://jabber.org/protocol/commands\"");
        if (getNode() != null) {
            buf.append(" node=\"").append(getNode()).append("\"");
        }
        buf.append(">");
        
        buf.append("<x xmlns=\"jabber:x:data\" type=\"result\">");
                
		synchronized (histories) {
			for (Item room : histories) {
				buf.append(room.toXml());
			}
		}
		buf.append("</x>");
		buf.append("</command>");
		return buf.toString();
	}

	public static class Item {
		
		private String body;
		private String timestamp;
		private String name;
		

		public Item()
		{
		}

		public Item(String name, String timestamp, String body)
		{
			this.name = name;
			this.body = body;
			this.timestamp = timestamp;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}
		
		public String getTimestamp()
		{
			return timestamp;
		}

		public void setTimestamp(String timestamp)
		{
			this.timestamp = timestamp;
		}
		
		public String getBody()
		{
			return body;
		}

		public void setBody(String body)
		{
			this.body = body;
		}

		public String toXml()
		{
			StringBuilder builder = new StringBuilder("<item body=\"");
			builder.append(body).append("\"");
	        builder.append(" timestamp=\"").append(timestamp).append("\"");
	        builder.append(" name=\"").append(name).append("\"/>");
//			builder.append("\"/>");
			return builder.toString();
		}

	}
}
