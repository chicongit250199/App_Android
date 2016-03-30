package antbuddy.htk.com.antbuddy2016.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import antbuddy.htk.com.antbuddy2016.objects.Domain;
import antbuddy.htk.com.antbuddy2016.util.JSONKey;

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

    public static List<Domain> parseToListDomains(String jsonString) {
        List<Domain> listDomains = new ArrayList<Domain>();
        JSONArray domainJsonArray = null;
        try {
            domainJsonArray = new JSONArray(jsonString);

            for (int i = 0; i < domainJsonArray.length(); i++) {
                try {
                    JSONObject element = (JSONObject) domainJsonArray.get(i);
                    String domainStr = element.getString(JSONKey.domain);
                    String nameStr = element.getString(JSONKey.name);
                    Domain domain = new Domain(domainStr, nameStr);
                    listDomains.add(domain);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listDomains;
    }
}
