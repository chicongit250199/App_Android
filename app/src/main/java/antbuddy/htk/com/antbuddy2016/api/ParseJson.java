package antbuddy.htk.com.antbuddy2016.api;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thanhnguyen on 30/03/2016.
 */
public class ParseJson {
    public static String getStringWithKey(String jsonString, String key) {
        String data = "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
            data = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
