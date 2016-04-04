/**
 * $RCSfile$
 * $Revision: $
 * $Date: $
 *
 * Copyright 2005-2007 Jive Software.
 *
 * All rights reserved. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.commands.AdHocCommand;
import org.jivesoftware.smackx.commands.AdHocCommand.Action;
import org.jivesoftware.smackx.commands.AdHocCommandNote;
import org.jivesoftware.smackx.packet.AdHocCommandData;
import org.jivesoftware.smackx.packet.AdHocCommandData.DropInMuteData;
import org.jivesoftware.smackx.packet.AdHocCommandData.DropInMuteData.ItemForDropInMute;
import org.jivesoftware.smackx.packet.AdHocCommandData.ItemCommon;
import org.jivesoftware.smackx.packet.AdHocCommandData.ItemForDropInCall;
import org.jivesoftware.smackx.packet.AdHocCommandData.ItemForDropInPermission;
import org.jivesoftware.smackx.packet.AdHocCommandData.ItemForHistory;
import org.jivesoftware.smackx.packet.AdHocCommandData.ItemForFeedPost;
import org.jivesoftware.smackx.packet.DataForm;
import org.xmlpull.v1.XmlPullParser;

/**
 * The AdHocCommandDataProvider parses AdHocCommandData packets.
 * 
 * @author Gabriel Guardincerri
 */
public class AdHocCommandDataProvider implements IQProvider {

	public IQ parseIQ(XmlPullParser parser) throws Exception {
		boolean done = false;
		AdHocCommandData adHocCommandData = new AdHocCommandData();
		DataFormProvider dataFormProvider = new DataFormProvider();

		int eventType;
		String elementName;
		String namespace;
		adHocCommandData.setSessionID(parser.getAttributeValue("", "sessionid"));
		adHocCommandData.setNode(parser.getAttributeValue("", "node"));

		// Status
		String status = parser.getAttributeValue("", "status");
		if (AdHocCommand.Status.executing.toString().equalsIgnoreCase(status)) {
			adHocCommandData.setStatus(AdHocCommand.Status.executing);
		}
		else if (AdHocCommand.Status.completed.toString().equalsIgnoreCase(status)) {
			adHocCommandData.setStatus(AdHocCommand.Status.completed);
		}
		else if (AdHocCommand.Status.canceled.toString().equalsIgnoreCase(status)) {
			adHocCommandData.setStatus(AdHocCommand.Status.canceled);
		}

		// Action
		String action = parser.getAttributeValue("", "action");
		if (action != null) {
			Action realAction = AdHocCommand.Action.valueOf(action);
			if (realAction == null || realAction.equals(Action.unknown)) {
				adHocCommandData.setAction(Action.unknown);
			}
			else {
				adHocCommandData.setAction(realAction);
			}
		}

		Object item;
		DropInMuteData _dropInMuteData = null;

		while (!done) {
			eventType = parser.next();
			elementName = parser.getName();
			namespace = parser.getNamespace();
			if (eventType == XmlPullParser.START_TAG) {
				if (parser.getName().equals("actions")) {
					String execute = parser.getAttributeValue("", "execute");
					if (execute != null) {
						adHocCommandData.setExecuteAction(AdHocCommand.Action.valueOf(execute));
					}
				}
				else if (parser.getName().equals("next")) {
					adHocCommandData.addAction(AdHocCommand.Action.next);
				}
				else if (parser.getName().equals("complete")) {
					adHocCommandData.addAction(AdHocCommand.Action.complete);
				}
				else if (parser.getName().equals("prev")) {
					adHocCommandData.addAction(AdHocCommand.Action.prev);
				}
				else if (!adHocCommandData.getNode().equals("11storage") &&
						!adHocCommandData.getNode().equals("grpstorage") &&
						!adHocCommandData.getNode().equals("dropincall") &&
						!adHocCommandData.getNode().equals("perm") &&
						!adHocCommandData.getNode().equals("feeds") &&
						!adHocCommandData.getNode().equals("dropinmute") &&
						elementName.equals("x") && namespace.equals("jabber:x:data")) {
					adHocCommandData.setForm((DataForm) dataFormProvider.parseExtension(parser));
				} else if (adHocCommandData.getNode().equals("11storage") && parser.getName().equals("item")) {
					// fetching chat history
					String _body = parser.getAttributeValue("", "body");
					String _timestamp = parser.getAttributeValue("", "timestamp");
					String _name = parser.getAttributeValue("", "name");
					String _stype = parser.getAttributeValue("", "stype");
					// result for chat action
					String _status = parser.getAttributeValue("", "status");
					String _desc = parser.getAttributeValue("", "desc");
					String _ack = parser.getAttributeValue("", "ack");

					if (_body != null && _timestamp != null && _name != null) {
						item = new ItemForHistory();
						((ItemForHistory) item)._name = _name;
						((ItemForHistory) item)._timestamp = _timestamp;
						((ItemForHistory) item)._body = _body;
						((ItemForHistory) item)._stype = _stype == null ? "" : _stype;
						((ItemForHistory) item)._ack = _ack == null ? "" : _ack;

						adHocCommandData.addItem(item);
					} else  if(_status != null && _desc != null) {
						item = new ItemCommon();
						((ItemCommon) item)._status = _status;
						((ItemCommon) item)._error = "";
						((ItemCommon) item)._desc = _desc;

						adHocCommandData.addItem(item);
					}
				} else if (adHocCommandData.getNode().equals("grpstorage") && parser.getName().equals("item")) {
					// fetching chat history
					String _body = parser.getAttributeValue("", "body");
					String _timestamp = parser.getAttributeValue("", "timestamp");
					String _name = parser.getAttributeValue("", "name");
					// result for chat action
					String _status = parser.getAttributeValue("", "status");
					String _error = parser.getAttributeValue("", "error");
					String _desc = parser.getAttributeValue("", "desc");

					if (_body != null && _timestamp != null && _name != null) {
						item = new ItemForHistory();
						((ItemForHistory) item)._name = _name;
						((ItemForHistory) item)._timestamp = _timestamp;
						((ItemForHistory) item)._body = _body;

						adHocCommandData.addItem(item);
					} else  if(_desc != null) {
						if (_status == null)
							_status = "";
						if (_error == null)
							_error = "";
						item = new ItemCommon();
						((ItemCommon) item)._status = _status;
						((ItemCommon) item)._error = _error;
						((ItemCommon) item)._desc = _desc;

						adHocCommandData.addItem(item);
					}
				} else if (adHocCommandData.getNode().equals("dropincall") && parser.getName().equals("item")) {
					// result for DropInCall action
					String _return = parser.getAttributeValue("", "return");
					String _desc = parser.getAttributeValue("", "desc");

					if (_return != null && _desc != null) {
						item = new ItemForDropInCall();
						((ItemForDropInCall) item)._return = _return;
						((ItemForDropInCall) item)._desc = _desc;

						adHocCommandData.addItem(item);
					}
				} else if (adHocCommandData.getNode().equals("perm") && parser.getName().equals("item")) {
					// result for toggle permission
					String _status = parser.getAttributeValue("", "status");
					String _error = parser.getAttributeValue("", "error");
					String _desc = parser.getAttributeValue("", "desc");
					// fetching DropInPermission
					String _p_priv_call = parser.getAttributeValue("", "p_priv_call");
					String _p_pub_call = parser.getAttributeValue("", "p_pub_call");
					String _p_follow = parser.getAttributeValue("", "p_follow");
					String _p_priv_chat = parser.getAttributeValue("", "p_priv_chat");
					String _p_dropin = parser.getAttributeValue("", "p_dropin");

					if (_p_priv_call != null) {
						item = new ItemForDropInPermission();
						((ItemForDropInPermission) item)._p_priv_call = _p_priv_call;
						((ItemForDropInPermission) item)._p_pub_call = _p_pub_call;
						((ItemForDropInPermission) item)._p_follow = _p_follow;
						((ItemForDropInPermission) item)._p_priv_chat = _p_priv_chat;
						((ItemForDropInPermission) item)._p_dropin = _p_dropin;

						adHocCommandData.addItem(item);
					} else if (_status != null) {
						item = new ItemCommon();
						if (_error == null)
							_error = "";
						((ItemCommon) item)._status = _status;
						((ItemCommon) item)._error = _error;
						((ItemCommon) item)._desc = _desc;

						adHocCommandData.addItem(item);
					}
				} else if (adHocCommandData.getNode().equals("feeds") && parser.getName().equals("item")) {
					String _body = parser.getAttributeValue("", "body");
					String _day = parser.getAttributeValue("", "day");
					String _hour = parser.getAttributeValue("", "hour");
					String _min = parser.getAttributeValue("", "min");
					String _sec = parser.getAttributeValue("", "sec");
					String _ts = parser.getAttributeValue("", "ts");
					String _url1 = parser.getAttributeValue("", "url1");
					String _url2 = parser.getAttributeValue("", "url2");
					String _userid = parser.getAttributeValue("", "userid");
					String _username = parser.getAttributeValue("", "username");
					String _fname = parser.getAttributeValue("", "fname");
					String _lname = parser.getAttributeValue("", "lname");

					if (_body != null && _day != null && _hour != null && _min != null && _sec != null) {
						item = new ItemForFeedPost();
						((ItemForFeedPost) item)._body = _body;
						((ItemForFeedPost) item)._day = _day;
						((ItemForFeedPost) item)._hour = _hour;
						((ItemForFeedPost) item)._min = _min;
						((ItemForFeedPost) item)._sec = _sec;
						((ItemForFeedPost) item)._ts = _ts;
						((ItemForFeedPost) item)._url1 = _url1;
						((ItemForFeedPost) item)._url2 = _url2;
						((ItemForFeedPost) item)._userid = _userid;
						((ItemForFeedPost) item)._username = _username;
						((ItemForFeedPost) item)._fname = _fname;
						((ItemForFeedPost) item)._lname = _lname;

						adHocCommandData.addItem(item);
					}
				} else if (adHocCommandData.getNode().equals("dropinmute")) {
					if (_dropInMuteData == null)
						_dropInMuteData = new DropInMuteData();
					if (parser.getName().equals("title"))
						_dropInMuteData._title = parser.nextText();

					if (parser.getName().equals("item")) {
						String _return = parser.getAttributeValue("", "return");
						String _mute_time = parser.getAttributeValue("", "mute_time");
						_dropInMuteData._itemForDropInMute = new ItemForDropInMute();
						_dropInMuteData._itemForDropInMute._return = _return;
						_dropInMuteData._itemForDropInMute._mute_time = _mute_time;
					}
					if (_dropInMuteData._itemForDropInMute != null && parser.getName().equals("value")) {
						_dropInMuteData._itemForDropInMute._value = parser.nextText();

						adHocCommandData.addItem(_dropInMuteData);
					}
				} else if (parser.getName().equals("note")) {
					AdHocCommandNote.Type type = AdHocCommandNote.Type.valueOf(
							parser.getAttributeValue("", "type"));
					String value = parser.nextText();
					adHocCommandData.addNote(new AdHocCommandNote(type, value));
				}
				else if (parser.getName().equals("error")) {
					XMPPError error = PacketParserUtils.parseError(parser);
					adHocCommandData.setError(error);
				}
			}
			else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("command")) {
					done = true;
				}
			}
		}
		return adHocCommandData;
	}

	public static class BadActionError implements PacketExtensionProvider {
		public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
			return new AdHocCommandData.SpecificError(AdHocCommand.SpecificErrorCondition.badAction);
		}
	}

	public static class MalformedActionError implements PacketExtensionProvider {
		public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
			return new AdHocCommandData.SpecificError(AdHocCommand.SpecificErrorCondition.malformedAction);
		}
	}

	public static class BadLocaleError implements PacketExtensionProvider {
		public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
			return new AdHocCommandData.SpecificError(AdHocCommand.SpecificErrorCondition.badLocale);
		}
	}

	public static class BadPayloadError implements PacketExtensionProvider {
		public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
			return new AdHocCommandData.SpecificError(AdHocCommand.SpecificErrorCondition.badPayload);
		}
	}

	public static class BadSessionIDError implements PacketExtensionProvider {
		public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
			return new AdHocCommandData.SpecificError(AdHocCommand.SpecificErrorCondition.badSessionid);
		}
	}

	public static class SessionExpiredError implements PacketExtensionProvider {
		public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
			return new AdHocCommandData.SpecificError(AdHocCommand.SpecificErrorCondition.sessionExpired);
		}
	}
}
