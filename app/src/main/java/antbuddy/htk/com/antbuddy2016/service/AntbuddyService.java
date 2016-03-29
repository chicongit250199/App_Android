//package antbuddy.htk.com.antbuddy2016.service;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//import android.os.RemoteException;
//import android.util.Log;
//
//import com.htk.antbuddy.model.ChatMessage;
//import com.htk.antbuddy.model.FileAntBuddy;
//import com.htk.antbuddy.model.OpeningChatRoom;
//import com.htk.antbuddy.model.Room;
//import com.htk.antbuddy.model.RouterInfo;
//import com.htk.antbuddy.model.User;
//import com.htk.antbuddy.model.UserInfo;
//import com.htk.antbuddy.utils.UserStateConstant;
//import com.htk.antbuddy.web.Request;
//
//import org.jivesoftware.smack.Roster;
//import org.jivesoftware.smack.packet.Presence;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class AntbuddyService extends Service {
//
//	private static final String TAG_THISCLASS = "AntbuddyService";
//
//	private AntbuddyXmppConnection mXmppConnection;
//
//	private IAntbuddyService.Stub rpcImpl = new IAntbuddyService.Stub() {
//
//		@Override
//		public String login(String cookie) throws RemoteException {
//			try {
//				Request.setCookie(cookie);
//				mXmppConnection = AntbuddyXmppConnection.getInstance();
//				UserInfo userInfo = mXmppConnection.getmUserInfo();
//				if (userInfo == null) {
//					String result = Request.connect("/api/users/me");
//					userInfo = UserInfo.parseUser(new JSONObject(result));
//					mXmppConnection.setmUserInfo(userInfo);
//				}
//				return mXmppConnection.connect(AntbuddyService.this, userInfo.getXmppUsername(), userInfo.getXmppPassword());
//			} catch (JSONException e){
//                e.printStackTrace();
//                Log.e(TAG_THISCLASS, "login()/ JSONException!! " + e.toString());
//                return "Error";
//            } catch (Exception e) {
//                Log.e(TAG_THISCLASS, "login()/ ERROR!! " + e.toString());
//				e.printStackTrace();
//				return "Error";
//			}
//		}
//
//		@Override
//		public String logout() throws RemoteException {
//			Request.setCookie(null);
//			AntbuddyXmppConnection.getInstance().disconnect();
//			return "Success";
//		}
//
//		@Override
//		public void reduceHistoryChat() throws RemoteException {
//
//		}
//
//		@Override
//		public String loadingRoom() throws RemoteException {
//			try {
//				String rooms = Request.connect("/api/rooms");
//
//				JSONArray roomArray = new JSONArray(rooms);
//				List<Room> listRoom = new ArrayList<Room>();
//				for (int i = 0; i < roomArray.length(); i++) {
//					listRoom.add(Room.parse((JSONObject) roomArray.get(i)));
//				}
//				mXmppConnection.setListRoom(listRoom);
//
//				String users = Request.connect("/api/users");
//				JSONArray userArray = new JSONArray(users);
//				List<User> listUser = new ArrayList<User>();
//				for (int i = 0; i < userArray.length(); i++) {
//					try {
//                        User user = User.parse((JSONObject) userArray.get(i));
//                        if(user.getActive()) {
//                            listUser.add(user);
//                        }
//					} catch (Exception e) {
//
//					}
//				}
//				mXmppConnection.setListUser(listUser);
//
//                Log.i("AntbuddyService_loadingRoom", "OK");
//				return "Success";
//			} catch (Exception e) {
//				e.printStackTrace();
//                Log.i("AntbuddyService_loadingRoom", "Request.connect() error");
//				return "Loading error";
//			}
//		}
//
//		@Override
//		public Map<String, List> getRouterInfo() throws RemoteException {
//			Map<String, List> map = new HashMap<>();
//
//			List<RouterInfo> listRouterInfo = new ArrayList<RouterInfo>();
//			List<Room> rooms = mXmppConnection.getListRoom();
//			List<User> users = mXmppConnection.getListUser();
//			UserInfo userInfo = mXmppConnection.getmUserInfo();
//			OpeningChatRoom currentRouter = mXmppConnection.getmCurrentRouter();
//			mXmppConnection.getmUserInfo().getListRoomsOpeningChat();
//
//			// listRouterInfo include: list rooms and users
//			for (Room room : rooms) {
//				listRouterInfo.add(new RouterInfo(room.get_id(), room.getName(), true, room.isPublic(), UserStateConstant.ON_ROOM, 0, room.getListUsers(), room.getPinMessage()));
//			}
//
//			Roster roster = mXmppConnection.getConnection().getRoster();
//
//			for (User user : users) {
//				Presence presence = roster.getPresence(user.get_id() + "@" + AntbuddyXmppConnection.DOMAIN);
//				listRouterInfo.add(new RouterInfo(user.get_id(), user.getName(), UserStateConstant.getState(presence.getType(), presence.getMode())));  // default all of users's icon are offline.
//			}
//
//			// All of things about rooms and users of User.
//			map.put("ListRouterInfo", listRouterInfo);
//
//			// Information about User. Can get OpeningChatRooms on there.
//			map.put("UserInfo", Arrays.asList(userInfo));
//			map.put("CurrentRouter", Arrays.asList(currentRouter));
//			return map;
//		}
//
//		@Override
//		public void sendMessage(ChatMessage messageChatting) throws RemoteException {
//			mXmppConnection.sendMessageOut(messageChatting);
//		}
//
//		@Override
//		public List<ChatMessage> getListHistoryMessages(String _jIdRouter) throws RemoteException {
//			String jid = _jIdRouter.split("@")[0];
//			UserInfo userInfo = mXmppConnection.getmUserInfo();
//			List<OpeningChatRoom> listRoomsOpeningChat = userInfo.getListRoomsOpeningChat();
//			for(OpeningChatRoom chatroom : listRoomsOpeningChat) {
//				if(chatroom.getChatRoomId().equals(jid)) {
//					mXmppConnection.setmCurrentRouter(chatroom);
//                    Log.i("AntbuddyService_getListHistoryMessages", "set current router=" + jid);
//					break;
//				}
//			}
//			return mXmppConnection.getListHistoryMessages(jid);
//		}
//
//		@Override
//		public List<ChatMessage> loadMore(RouterInfo router, boolean isOnChangeClick) throws RemoteException {
//            UserInfo userInfo = mXmppConnection.getmUserInfo();
//            List<OpeningChatRoom> listRoomsOpeningChat = userInfo.getListRoomsOpeningChat();
//            for(OpeningChatRoom chatroom : listRoomsOpeningChat) {
//                if(chatroom.getChatRoomId().equals(router.get_id())) {
//                    mXmppConnection.setmCurrentRouter(chatroom);
//                    mXmppConnection.resetNotificationCount(router.get_id());
//                    Log.i("AntbuddyService_getListHistoryMessages", "set current router=" + router.get_id());
//                    break;
//                }
//            }
//
//			List<ChatMessage> historys = mXmppConnection.getListHistoryMessages(router.get_id());
//			if(isOnChangeClick && historys != null && historys.size() > 0) {
//				return mXmppConnection.getListHistoryMessages(router.get_id());
//			} else {
//				return mXmppConnection.getMoreListHistoryMessages(router);
//			}
//		}
//
//        @Override
//        public void upLoadFile(String path, ChatMessage messageChatting) throws RemoteException {
//                JSONObject json = null;
//                try {
//                    json = Request.upLoadFile("http://" + AntbuddyXmppConnection.HOST + "/api/files", path);
//                /* Anh Hien huong dan them send message o day */
//                /*
//                message.setFileAntBuddy(new FileAntBuddy());
//                mXmppConnection.sendMessageOut(message);
//                */
//                    Log.i("AntbuddyService_upLoadFile", "Check json?? = " + json.toString());
//
//                    FileAntBuddy fileAntBuddy = new FileAntBuddy();
//                    fileAntBuddy.setName(json.getString("name"));
//                    fileAntBuddy.setMimeType(json.getString("mimeType"));
//                    fileAntBuddy.setSize(json.getInt("size"));
//                    fileAntBuddy.setFileUrl(json.getString("fileUrl"));
//                    fileAntBuddy.setThumbnailUrl(json.getString("thumbnailUrl"));
//
//                    Log.i("AntbuddyService_upLoadFile", "setName = " + fileAntBuddy.getName());
//
//                    messageChatting.setFileAntBuddy(fileAntBuddy);
//                    //messageChatting.setBody("" + fileAntBuddy.getFileUrl());
//                    sendMessage(messageChatting);
//
//                } catch (Exception e ) {
//                    e.printStackTrace();
//                }
//        }
//
//        @Override
//		public void deleteOpenChat(String idRouter) throws RemoteException {
//			List<OpeningChatRoom> listOpenChatRoom = mXmppConnection.getmUserInfo().getListRoomsOpeningChat();
//			Log.i(TAG_THISCLASS, "deleteOpenChat()/listOpenChatRoom: " + listOpenChatRoom.size());
//			for (OpeningChatRoom chatroom : listOpenChatRoom) {
//				if(chatroom.getChatRoomId().equals(idRouter)) {
//					listOpenChatRoom.remove(chatroom);
//					break;
//				}
//			}
//			Log.i(TAG_THISCLASS, "deleteOpenChat()/listOpenChatRoom: after" + listOpenChatRoom.size());
//		}
//
//		@Override
//		public void addOpenChat(RouterInfo router) throws RemoteException {
//			List<OpeningChatRoom> listOpenChatRoom = mXmppConnection.getmUserInfo().getListRoomsOpeningChat();
//
//			Log.d(TAG_THISCLASS, "addOpenChat()/listOpenChatRoom: " + listOpenChatRoom.size());
//			Log.e(TAG_THISCLASS, "ADDDDD: " + router.getAllInformations());
//			listOpenChatRoom.add(new  OpeningChatRoom(router.get_id(), router.get_id(), router.getIsMuc()));
//			Log.d(TAG_THISCLASS, "addOpenChat()/listOpenChatRoom after: " + listOpenChatRoom.size());
//		}
//
//        @Override
//        public void clearCurrentRouter() throws RemoteException {
//            mXmppConnection.setmCurrentRouter(null);
//        }
//
//	};
//
//	@Override
//	public IBinder onBind(Intent intent) {
//		Log.i(TAG_THISCLASS, "onBind");
//		return rpcImpl;
//	}
//
//	@Override
//	public void onCreate() {
//		super.onCreate();
//		Log.i(TAG_THISCLASS, "onCreate");
//	}
//
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		Log.i(TAG_THISCLASS, "onStartCommand");
//		return START_STICKY;
//	}
//}
