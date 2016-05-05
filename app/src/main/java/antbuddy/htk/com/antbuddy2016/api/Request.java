package antbuddy.htk.com.antbuddy2016.api;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import antbuddy.htk.com.antbuddy2016.interfaces.HttpRequestReceiver;
import antbuddy.htk.com.antbuddy2016.model.FileAntBuddy;
import antbuddy.htk.com.antbuddy2016.service.AntbuddyApplication;
import antbuddy.htk.com.antbuddy2016.setting.ABSharedPreference;
import antbuddy.htk.com.antbuddy2016.util.LogHtk;

/**
 * Created by thanhnguyen on 05/05/2016.
 */
public class Request {
    public void updateLoadFile(File file, final HttpRequestReceiver<FileAntBuddy> receiver) {

        JSONObject jsonObject = upLoadFile(AntbuddyApplication.getInstance().getURL(), file);
        if (jsonObject != null) {
            LogHtk.i(LogHtk.Test1, "-->jsonObject: " + jsonObject.toString());
            receiver.onSuccess(new FileAntBuddy((jsonObject)));
        } else {
            receiver.onError("Can not upload File!");
        }
    }

    private static JSONObject upLoadFile(String urlStr, File fileUpload) {
        try{
            urlStr = urlStr + "/api/files/";
            LogHtk.i(LogHtk.Test1, "-->urlStr: " + urlStr);
            LogHtk.i(LogHtk.Test1, "fileUpload: " + fileUpload.getPath());
            URL url = new URL(urlStr);
            HttpsURLConnection httpCon = (HttpsURLConnection) url.openConnection();

            httpCon.setRequestProperty("authorization", ABSharedPreference.getAccountConfig().getToken());
            httpCon.setRequestMethod("POST");

            // set timeout
            httpCon.setConnectTimeout(10 * 1000); //TIMEOUT_SECOND * 10
            httpCon.setReadTimeout(10 * 1000); //TIMEOUT_SECOND * 1000

            // set entry
            String fileExtension = getFilenameExtension(fileUpload.getPath());
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);

            LogHtk.i(LogHtk.Test1, "fileExtension: " + fileExtension);
            LogHtk.i(LogHtk.Test1, "mimeType: " + mimeType);
            if (mimeType == null) {
                LogHtk.i(LogHtk.Test1, "mimeType is Null!  ");
                mimeType = fileExtension;
            }

            ContentBody fb = new FileBody(fileUpload, mimeType);
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.STRICT);
            entity.addPart("file", fb);
            httpCon.addRequestProperty(entity.getContentType().getName(), entity.getContentType().getValue());
            OutputStream os = httpCon.getOutputStream();
            entity.writeTo(httpCon.getOutputStream());
            os.close();

            // request to server
            httpCon.connect();
            LogHtk.i(LogHtk.Test1, "httpCon.getResponseCode() = " + httpCon.getResponseCode());
            if (httpCon.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String strResponse = readStream(httpCon.getInputStream());
                LogHtk.i(LogHtk.Test1, "111");
                return new JSONObject(strResponse);
            }
            LogHtk.i(LogHtk.Test1, "222");

        } catch (Exception e) {
            LogHtk.i(LogHtk.Test1, "333");
            e.printStackTrace();
        }
        LogHtk.i(LogHtk.Test1, "444");
        return null;
    }

    private static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }

    private static String getFilenameExtension(String fileName) {
        String extension = null;
        if (!TextUtils.isEmpty(fileName)) {
            int lastDot = fileName.lastIndexOf('.');
            if ((lastDot > 0) && (lastDot < fileName.length() - 1)) {
                extension = fileName.substring(lastDot + 1).toLowerCase();
            }
        }
        return extension;
    }
}
