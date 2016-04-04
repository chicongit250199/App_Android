package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.PrivateRoomItems;
import org.xmlpull.v1.XmlPullParser;

public class FetchRoomListProvider implements IQProvider {

    public IQ parseIQ(XmlPullParser parser) throws Exception {
        PrivateRoomItems privateRoomItems = new PrivateRoomItems();
        boolean done = false;
        PrivateRoomItems.Item item;
        String name = "";
        String subject = "";
        String avatar = "";
        String admin = "";
        String node = "";
        privateRoomItems.setNode(parser.getAttributeValue("", "node"));
        while (!done) {
            int eventType = parser.next();

            if (eventType == XmlPullParser.START_TAG && "room".equals(parser.getName())) {
                // Initialize the variables from the parsed XML
                name = parser.getAttributeValue("", "name");
                subject = parser.getAttributeValue("", "subject");
                avatar = parser.getAttributeValue("", "avatar");
                admin = parser.getAttributeValue("", "admin");
            }
            else if (eventType == XmlPullParser.END_TAG && "room".equals(parser.getName())) {
                // Create a new Item and add it to DiscoverItems.
                item = new PrivateRoomItems.Item();
                item._name = name;
                item._subject = subject;
                item._avatar = avatar;
                item._admin = admin;
                privateRoomItems.addItem(item);
            }
            else if (eventType == XmlPullParser.END_TAG && "query".equals(parser.getName())) {
                done = true;
            }
        }
        return privateRoomItems;
    }
}