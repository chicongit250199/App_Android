package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.IQ;

public class UserActiveResponse extends IQ {

	private String node;
	private String response;
	private boolean isSetResponse;
	
	private String seconds;
	private String days;
	private String minutes;
	private String hours;


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
	
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
	
	public boolean getIsSetResponse() {
		return isSetResponse;
	}

	public void setIsSetResponse(boolean isSetResponse) {
		this.isSetResponse = isSetResponse;
	}
	
	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}
	
	public String getSeconds() {
		return seconds;
	}

	public void setSeconds(String seconds) {
		this.seconds = seconds;
	}
	
	public String getMinutes() {
		return minutes;
	}

	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}
	
	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}


	public UserActiveResponse()
	{
		isSetResponse = false;		
	}

	@Override
	public String getChildElementXML()
	{
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"http://jabber.org/protocol/lastactive\"");
        if (getNode() != null) {
            buf.append(" node=\"");
            buf.append(getNode());
            buf.append("\"");
        }
        buf.append(">");
        
        if (isSetResponse) {
        	buf.append("<success/>");            
        } else {
        	buf.append("<last_active seconds=\"").append(seconds).append("\" days=\"").append(days).
        	append("\" minutes=\"").append(minutes).append("\" hours=\"").append(hours).append("\"/>");
        }
		buf.append("</query>");
		return buf.toString();
	}
}
