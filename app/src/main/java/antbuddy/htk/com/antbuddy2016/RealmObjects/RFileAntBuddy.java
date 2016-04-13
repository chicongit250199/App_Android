package antbuddy.htk.com.antbuddy2016.RealmObjects;

import io.realm.RealmObject;

/**
 * Created by thanhnguyen on 12/04/2016.
 */
public class RFileAntBuddy extends RealmObject {
    private String name;           //google_play_services.zip
    private int size;		   //16223844
    private String fileUrl;        //http://s3-ap-southeast-1.amazonaws.com/hipchat/google_play_services1415098114018_google_play_services.zip
    private String mimeType;       //application/octet-stream
    private String thumbnailUrl;
    private int thumbnailWidth;
    private int thumbnailHeight;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(int thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }

    public int getThumbnailHeight() {
        return thumbnailHeight;
    }

    public void setThumbnailHeight(int thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight;
    }
}
