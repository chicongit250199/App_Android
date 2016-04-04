package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.IQ;

public class PublicCallMuteResponse extends IQ {

	public String successStr;
	public String failedStr, descStr;
	public String muteStr, tsStr;

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<query xmlns=\"public_call_mute\">");

		if (successStr != null) {
			buf.append("<success/>");
		} else if (failedStr != null && descStr != null) {
			buf.append("<failed desc=\"" + descStr + "\"/>");
		} else if (muteStr != null && tsStr != null) {
			buf.append("<mute ts=\"" + tsStr + "\"/>");
		}
		buf.append("</query>");

		return buf.toString();
	}
}
