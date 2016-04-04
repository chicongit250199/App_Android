package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.PrivateRoomItems;
import org.jivesoftware.smackx.packet.SetRoomTypeIQ;
import org.xmlpull.v1.XmlPullParser;

public class SetRoomTypeProvider implements IQProvider {

    public IQ parseIQ(XmlPullParser parser) throws Exception {
    	SetRoomTypeIQ roomtypeIQ = new SetRoomTypeIQ();
    	return roomtypeIQ;
    }
}