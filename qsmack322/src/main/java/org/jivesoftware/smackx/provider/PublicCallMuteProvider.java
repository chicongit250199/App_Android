package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.PublicCallMuteResponse;
import org.xmlpull.v1.XmlPullParser;

public class PublicCallMuteProvider implements IQProvider {

	public IQ parseIQ(XmlPullParser parser) throws Exception {
		PublicCallMuteResponse muteResponse = new PublicCallMuteResponse();
		boolean done = false;
		String successStr = null;
		String failedStr = null, descStr = null;
		String muteStr = null, tsStr = null;

		while (!done) {
			int eventType = parser.next();

			if (eventType == XmlPullParser.START_TAG && parser.getName().equals("success")) {
				// Initialize the variables from the parsed XML
				successStr = "";
			} else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("failed")) {
				// Initialize the variables from the parsed XML
				failedStr = "";
				descStr = parser.getAttributeValue("", "desc");
			} else if (eventType == XmlPullParser.START_TAG && parser.getName().equals("mute")) {
				// Initialize the variables from the parsed XML
				muteStr = "";
				tsStr = parser.getAttributeValue("", "ts");
			}
			else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("success")) {
				muteResponse.successStr = successStr;
			} else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("failed")) {
				muteResponse.failedStr = failedStr;
				muteResponse.descStr = descStr;
			} else if (eventType == XmlPullParser.END_TAG && parser.getName().equals("mute")) {
				muteResponse.muteStr = muteStr;
				muteResponse.tsStr = tsStr;
			}
			else if (eventType == XmlPullParser.END_TAG && "query".equals(parser.getName())) {
				done = true;
			}
		}
		return muteResponse;
	}
}