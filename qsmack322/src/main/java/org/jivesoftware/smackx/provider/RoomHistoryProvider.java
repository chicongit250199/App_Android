package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.PrivateRoomItems;
import org.jivesoftware.smackx.packet.RoomHistory;
import org.xmlpull.v1.XmlPullParser;

public class RoomHistoryProvider implements IQProvider {

    public IQ parseIQ(XmlPullParser parser) throws Exception {
        RoomHistory roomHistory = new RoomHistory();
        boolean done = false;
        RoomHistory.Item item;
        String name = "";
        String body = "";
        String timestamp = "";
        String node = "";
        roomHistory.setNode(parser.getAttributeValue("grpstorage", "node"));
        while (!done) {
            int eventType = parser.next();

            if (eventType == XmlPullParser.START_TAG && "item".equals(parser.getName())) {
                // Initialize the variables from the parsed XML
                body = parser.getAttributeValue("", "body");
                timestamp = parser.getAttributeValue("", "timestamp");
                name = parser.getAttributeValue("", "name");
            }
            else if (eventType == XmlPullParser.END_TAG && "item".equals(parser.getName())) {
                // Create a new Item and add it to DiscoverItems.
                item = new RoomHistory.Item(name, timestamp, body);
                roomHistory.addItem(item);
            }
            else if (eventType == XmlPullParser.END_TAG && "command".equals(parser.getName())) {
                done = true;
            }
        }
        return roomHistory;
    }
}