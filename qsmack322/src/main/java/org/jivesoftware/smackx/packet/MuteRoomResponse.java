package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.IQ;

public class MuteRoomResponse extends IQ {

	private String node;
	public String typeStr;
	public String valueStr;

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
	
	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"http://jabber.org/protocol/muc#mute\"");
        if (getNode() != null) {
            buf.append(" node=\"");
            buf.append(getNode());
            buf.append("\"");
        }
        buf.append(">");
                
		buf.append("<item type=\"").append(typeStr == null ? "" : typeStr).append("\" ");
		buf.append("value=\"").append(valueStr == null ? "" : valueStr).append("\"/>");
		buf.append("</query>");
		return buf.toString();
	}
}
