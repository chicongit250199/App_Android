package antbuddy.htk.com.antbuddy2016.GsonObjects;

/**
 * Created by thanhnguyen on 13/04/2016.
 */

//"name": "device-2016-04-12-140855.png",
////        "size": 225369,
////        "fileUrl": "https://abs1.antbuddy.com/antbuddy-bucket/1460444887543_device20160412140855.png",
////        "mimeType": "image/png",
////        "thumbnailUrl": "https://abs1.antbuddy.com/antbuddy-bucket/thumb_1460444887543_device20160412140855.png",
////        "thumbnailWidth": 128,
////        "thumbnailHeight": 228

public class GFileAntBuddy {
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
