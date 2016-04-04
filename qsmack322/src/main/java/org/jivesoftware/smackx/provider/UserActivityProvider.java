package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.UserActiveResponse;
import org.xmlpull.v1.XmlPullParser;

public class UserActivityProvider implements IQProvider {

    public IQ parseIQ(XmlPullParser parser) throws Exception {
        UserActiveResponse activeResponse = new UserActiveResponse();
        boolean done = false;
        boolean isSetResponse = false;
        String node = "";
        String seconds = "";
        String days = "";
        String minutes = "";
        String hours = "";
        
        activeResponse.setNode(parser.getAttributeValue("", "node"));
        while (!done) {
            int eventType = parser.next();

            if (eventType == XmlPullParser.START_TAG && "success".equals(parser.getName())) {
                // Initialize the variables from the parsed XML
                isSetResponse = true;
                activeResponse.setIsSetResponse(isSetResponse);
            }
            else if (eventType == XmlPullParser.START_TAG && "last_active".equals(parser.getName())) {
                // Create a new Item and add it to DiscoverItems.
            	seconds = parser.getAttributeValue("", "seconds");
            	days = parser.getAttributeValue("", "days");
            	minutes = parser.getAttributeValue("", "minutes");
            	hours = parser.getAttributeValue("", "hours");
            }
            else if (eventType == XmlPullParser.END_TAG && "last_active".equals(parser.getName())) {
                // Create a new Item and add it to DiscoverItems.
            	activeResponse.setSeconds(seconds);
            	activeResponse.setDays(days);
            	activeResponse.setMinutes(minutes);
            	activeResponse.setHours(hours);
            }
            else if (eventType == XmlPullParser.END_TAG && "query".equals(parser.getName())) {
                done = true;
            }
        }
        return activeResponse;
    }
}