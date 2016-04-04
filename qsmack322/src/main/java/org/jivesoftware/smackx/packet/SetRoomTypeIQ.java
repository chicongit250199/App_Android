package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.IQ;

public class SetRoomTypeIQ extends IQ {

	@Override
	public String getChildElementXML()
	{
		StringBuilder buf = new StringBuilder();
        buf.append("<query xmlns=\"http://jabber.org/protocol/muc#roomtype\"><room type=\"private\"/></query>");
        return buf.toString();
	}
}
