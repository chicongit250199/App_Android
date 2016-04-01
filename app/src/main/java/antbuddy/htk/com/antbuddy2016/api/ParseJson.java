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
    public static String getStringWithKey(JSONObject jsonObject, String key) {
        String data = "";
        try {
            data = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<Domain> parseToListDomains(JSONArray jsonArray) {
        List<Domain> listDomains = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject element = (JSONObject) jsonArray.get(i);
                String domainStr = element.getString(JSONKey.domain);
                String nameStr = element.getString(JSONKey.name);
                Domain domain = new Domain(domainStr, nameStr);
                listDomains.add(domain);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listDomains;
    }
}
