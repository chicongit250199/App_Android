package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.MuteRoomResponse;
import org.xmlpull.v1.XmlPullParser;

public class MuteRoomProvider implements IQProvider {

    public IQ parseIQ(XmlPullParser parser) throws Exception {
        MuteRoomResponse muteResponse = new MuteRoomResponse();
        boolean done = false;
        String typeStr = "";
        String valueStr = "";
        String node = "";
        muteResponse.setNode(parser.getAttributeValue("", "node"));
        while (!done) {
            int eventType = parser.next();

            if (eventType == XmlPullParser.START_TAG && "item".equals(parser.getName())) {
                // Initialize the variables from the parsed XML
                typeStr = parser.getAttributeValue("", "type");
                valueStr = parser.getAttributeValue("", "value");
            }
            else if (eventType == XmlPullParser.END_TAG && "item".equals(parser.getName())) {
                // Create a new Item and add it to DiscoverItems.
            	muteResponse.typeStr = typeStr;
            	muteResponse.valueStr = valueStr;
            }
            else if (eventType == XmlPullParser.END_TAG && "query".equals(parser.getName())) {
                done = true;
            }
        }
        return muteResponse;
    }
}