package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.PrivateRoomItems;
import org.jivesoftware.smackx.packet.RoomMembers;
import org.xmlpull.v1.XmlPullParser;

public class RoomMembersListProvider implements IQProvider {

    public IQ parseIQ(XmlPullParser parser) throws Exception {
        RoomMembers roomMembers = new RoomMembers();
        boolean done = false;
        RoomMembers.Item item;
        String jid = "";
        String affiliation = "";
        String firstname = "";
        String lastname = "";
        String username = "", url1 = "", url2 = "";
        
        String node = "";
        roomMembers.setNode(parser.getAttributeValue("", "node"));
        while (!done) {
            int eventType = parser.next();

            if (eventType == XmlPullParser.START_TAG && "subscriber".equals(parser.getName())) {
                // Initialize the variables from the parsed XML
            	jid = parser.getAttributeValue("", "jid");
            	affiliation = parser.getAttributeValue("", "affiliation");
            	firstname = parser.getAttributeValue("", "firstname");
            	lastname = parser.getAttributeValue("", "lastname");
            	username = parser.getAttributeValue("", "username");
            	url1 = parser.getAttributeValue("", "url1");
            	url2 = parser.getAttributeValue("", "url2");
            }
            else if (eventType == XmlPullParser.END_TAG && "subscriber".equals(parser.getName())) {
                // Create a new Item and add it to DiscoverItems.
                item = new RoomMembers.Item(jid, affiliation, firstname, lastname, username, url1, url2);
                roomMembers.addItem(item);
            }
            else if (eventType == XmlPullParser.END_TAG && "query".equals(parser.getName())) {
                done = true;
            }
        }
        return roomMembers;
    }
}