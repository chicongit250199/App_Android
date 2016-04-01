package antbuddy.htk.com.antbuddy2016.api;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.util.Constants;
import antbuddy.htk.com.antbuddy2016.util.RequestKey;

//import org.apache.http.entity.mime.content.ContentBody;

public class ABRequest {
    public static final String TAG_THISCLASS = "ABRequest";

    //private static final String URL_SEND_MESSAGE_OUT = "http://" + AntbuddyXmppConnection.HOST + "/api/messages";
   // private static final String URL_DELETE_OPENING_CHAT_ROOM = "http://" + AntbuddyXmppConnection.HOST + "/api/users/me/openingChatRoom/";
    //private static final String URL_OPEN_OPENING_CHAT_ROOM = "http://" + AntbuddyXmppConnection.HOST + "/api/users/me/openingChatRooms";

    private static String cookie = "";
    public static final int TIMEOUT_SECOND = 10;

//    public static enum METHOD_URL {
//        POST, GET, DELETE
//    }

    public static enum RESPONSE_RESULT {
        OK, Created, EMPTY_RESPONSE, ERROR_REQUEST
    }

    public static String getCookie() {
        return cookie;
    }

    public static void setCookie(String cookie) {
        ABRequest.cookie = cookie;
    }

//    public static String connect(String urlString) {
//        String urlFull = "http://" + AntbuddyXmppConnection.HOST + "" + urlString;
//        return (String) requestURL(urlFull, METHOD_URL.GET, null);
//    }

//    public static JSONObject upLoadFile(String url, String path_file) {
//        JSONObject json = null;
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost(url);
//        httppost.addHeader("Cookie", cookie);
//
//        File file = new File(path_file);
//
//        Log.i("Request:upLoadFile", "====>path_file = " + path_file);
//        Uri selectedUri = Uri.fromFile(file);
//        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedUri.toString());
//        String mimeType= MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
//        ContentBody fb = new FileBody(file, mimeType);
//        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.STRICT);
//        entity.addPart("file", fb);
//
//        httppost.setEntity(entity);
//        HttpResponse response = null;
//        try {
//            response = httpclient.execute(httppost);
//
//        } catch (ClientProtocolException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        InputStream instream;
//        try {
//            HttpEntity httpEntity = response.getEntity();
//            if (entity != null) {
//
//                instream = httpEntity.getContent();
//                String s_return = readResponse(instream);
//                Log.i("Request:upLoadFile", "s_return = " + s_return);
//                json = new JSONObject(s_return);
//                instream.close();
//            }
//        } catch (IllegalStateException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return json;
//
//    }
//
//    public static String requestURLOpenOpeningChatRoom(RouterInfo clickedRouteInfo) {
//        String result = null;
//        try {
//            // add Json
//            JSONObject jsonObj = new JSONObject();
//            jsonObj.put("chatRoomId", clickedRouteInfo.get_id());
//            jsonObj.put("isMuc", clickedRouteInfo.getIsMuc());
//
//            result = Request.requestURL(URL_OPEN_OPENING_CHAT_ROOM, METHOD_URL.POST, jsonObj);
//        } catch (JSONException e) {
//            Log.e(TAG_THISCLASS, "ERROR! JSONException/ " + e.toString());
//            result = RESPONSE_RESULT.ERROR_REQUEST + "";
//        }  catch (Exception e) {
//            e.printStackTrace();
//            result = RESPONSE_RESULT.ERROR_REQUEST + "";
//        }
//        return result;
//    }
//
//    public static String requestURLDeleteOpeningChatRoom(String idRoom) {
//        String urlFull = URL_DELETE_OPENING_CHAT_ROOM + idRoom;
//        return (String) Request.requestURL(urlFull, Request.METHOD_URL.DELETE, null);
//    }
//
//    public static String requestURLSendMessageOut(ChatMessage chatMessageOut) {
//        String result = null;
//        try {
//            JSONObject jsonMessageChatting = new JSONObject();
//            jsonMessageChatting.put("body", chatMessageOut.getBody());
//            jsonMessageChatting.put("fromId", chatMessageOut.getReceiverId());
//            jsonMessageChatting.put("id", UUID.randomUUID());
//            if (chatMessageOut.getType().equals(ChatMessage.TYPE.chat.toString())) {
//                jsonMessageChatting.put("receiverId", chatMessageOut.getReceiverId());
//            } else {
//                jsonMessageChatting.put("receiverId", JSONObject.NULL);
//            }
//            jsonMessageChatting.put("senderId", chatMessageOut.getSenderId());
//            jsonMessageChatting.put("type", chatMessageOut.getType());
//
//            if(chatMessageOut.getFileAntBuddy() != null) {
//                JSONObject fileElements = new JSONObject();
//                fileElements.put("fileUrl", chatMessageOut.getFileAntBuddy().getFileUrl());
//                fileElements.put("mimeType", chatMessageOut.getFileAntBuddy().getMimeType());
//                fileElements.put("name", chatMessageOut.getFileAntBuddy().getName());
//                fileElements.put("size", chatMessageOut.getFileAntBuddy().getSize());
//                fileElements.put("thumbnailUrl", chatMessageOut.getFileAntBuddy().getThumbnailUrl());
//                jsonMessageChatting.put("body","File uploaded: "+chatMessageOut.getFileAntBuddy().getThumbnailUrl());
//                jsonMessageChatting.put("file", fileElements);
//            }
//
//            result = (String) requestURL(URL_SEND_MESSAGE_OUT, METHOD_URL.POST, jsonMessageChatting);
//        } catch (JSONException e) {
//            Log.e(TAG_THISCLASS, "ERROR! JSONException/ " + e.toString());
//            result = RESPONSE_RESULT.ERROR_REQUEST + "";
//        }  catch (Exception e) {
//            e.printStackTrace();
//            result = RESPONSE_RESULT.ERROR_REQUEST + "";
//        }
//        return result;
//    }

    protected static void POSTLogin(String email, String password, final HttpRequestReceiver receiver) {
        final String emailStr = email.trim();
        final String passwordStr = password.trim();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(RequestKey.email, emailStr);
            jsonObject.put(RequestKey.password, passwordStr);
        } catch (JSONException e) {
            receiver.onError("Can not create Json Object!");
            e.printStackTrace();
            return;
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, API.LOGIN_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        receiver.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        receiver.onError(error.toString());
                    }
                });

        // Adding request to request queueca tca tuan
        AntbuddyApplication.getInstance().addToRequestQueue(req);
    }

    protected static void GETOrganizations(final HttpRequestReceiver receiver) {
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, API.API_ORGANIZATIONS_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        receiver.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        receiver.onError(error.toString());
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("authorization", Constants.token);
                        return params;
            }
        };
        AntbuddyApplication.getInstance().addToRequestQueue(req);
    }

    protected static void GETOrganizationUserProfile(final HttpRequestReceiver receiver) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, API.API_USERS_ME_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        receiver.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        receiver.onError(error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", Constants.token);
                return params;
            }
        };
        AntbuddyApplication.getInstance().addToRequestQueue(req);
    }

//    private static String requestURL(String urlFull, METHOD_URL methodRequest, JSONObject jsonObject) {
//        String responseStr = "";
//
//        //POST, GET, DELETE, ...
//        String methodStr = methodRequest.toString();
//        try {
//            Log.d(TAG_THISCLASS, "URL request: " + urlFull + ", Method: " + methodStr);
//            URL url = new URL(urlFull);
//            HttpURLConnection httpCon;
//            httpCon = (HttpURLConnection) url.openConnection();
//            httpCon.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//            httpCon.setRequestProperty("Cookie", cookie);
//            httpCon.setRequestMethod(methodStr);
//
//            // set timeout
//            httpCon.setConnectTimeout(TIMEOUT_SECOND * 1000); //TIMEOUT_SECOND * 10
//            httpCon.setReadTimeout(TIMEOUT_SECOND * 1000); //TIMEOUT_SECOND * 1000
//
//            // request to server
//            httpCon.connect();
//            // if have para/json
//            if (jsonObject != null) {
//                String message = jsonObject.toString();
//                Log.d(TAG_THISCLASS, ".....Json Request: " + message);
//                //setup send
//                OutputStream os = new BufferedOutputStream(httpCon.getOutputStream());
//                os.write(message.getBytes());
//                //clean up
//                os.flush();
//            }
//            //get response
//            int sc = httpCon.getResponseCode();
//            if (sc == 200 || sc == 201) {
//                Log.d(TAG_THISCLASS, "getResponseCode: " + sc);
//                InputStream is = httpCon.getInputStream();
//                responseStr = readResponse(is);
//                if (responseStr.equals("")) {
//                    responseStr = RESPONSE_RESULT.EMPTY_RESPONSE + "";
//                }
//                is.close();
//            } else {
//                responseStr = RESPONSE_RESULT.ERROR_REQUEST + "";
//                Log.e(TAG_THISCLASS, "getResponseCode: " + sc);
//            }
//        } catch (IllegalArgumentException e) {
//            Log.e(TAG_THISCLASS, "ERROR! Connect Server Timeout/ " + e.toString());
//            responseStr = RESPONSE_RESULT.ERROR_REQUEST + "";
//        } catch (MalformedURLException e) {
//            Log.e(TAG_THISCLASS, "ERROR! MalformedURLException/ " + e.toString());
//            responseStr = RESPONSE_RESULT.ERROR_REQUEST + "";
//        } catch (IOException e) {
//            Log.e(TAG_THISCLASS, "ERROR! IOException/ " + e.toString());
//            responseStr = RESPONSE_RESULT.ERROR_REQUEST + "";
//        } catch (Exception e) {
//            Log.e(TAG_THISCLASS, "ERROR! Exception/ " + e.toString());
//            responseStr = RESPONSE_RESULT.ERROR_REQUEST + "";
//        }
//        return responseStr;
//    }

    /**
     * Reads the response from the input stream and returns it as a string.
     */
    private static String readResponse(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data = new byte[2048];
        int len;
        while ((len = is.read(data, 0, data.length)) >= 0) {
            bos.write(data, 0, len);
        }
        return new String(bos.toByteArray(), "UTF-8");
    }
}