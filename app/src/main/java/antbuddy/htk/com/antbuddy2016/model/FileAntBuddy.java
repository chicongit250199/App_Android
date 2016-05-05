package antbuddy.htk.com.antbuddy2016.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.jivesoftware.smack.packet.AntBuddyFile;
import org.json.JSONObject;

import antbuddy.htk.com.antbuddy2016.util.AndroidHelper;

public class FileAntBuddy implements Parcelable {
	
	private final static String key_name = "name";
	private final static String key_size = "size";
	private final static String key_fileUrl = "fileUrl";
	private final static String key_mimeType = "mimeType";
	private final static String key_thumbnailUrl = "thumbnailUrl";
	private final static String key_thumbnailWidth = "thumbnailWidth";
	private final static String key_thumbnailHeight = "thumbnailHeight";

//	{
//		"name": "aaa.jks",
//			"size": 2131,
//			"fileUrl": "https://abs1.antbuddy.com/antbuddy-bucket/1462351753641_aaa.jks",
//			"mimeType": "application/x-java-keystore",
//			"thumbnailUrl": "",
//			"thumbnailWidth": 128,
//			"thumbnailHeight": 128
//	}
//

	public static final String TAG_THISCLASS = "FileAntBuddy";
	private String name;           //google_play_services.zip
	private int size;		   //16223844
	private String fileUrl;        //http://s3-ap-southeast-1.amazonaws.com/hipchat/google_play_services1415098114018_google_play_services.zip
	private String mimeType;       //application/octet-stream
	private String thumbnailUrl;
	private int thumbnailWidth;
	private int thumbnailHeight;

	public FileAntBuddy() {
	}
	
	public FileAntBuddy(String name, int size, String fileUrl, String mimeType, String thumbnailUrl) {
		this.name = name;
		this.size = size;
		this.fileUrl = fileUrl;
		this.mimeType = mimeType;
		this.thumbnailUrl = thumbnailUrl;
	}
	
	public FileAntBuddy(JSONObject json) {
		name = AndroidHelper.getString(json, key_name, null);
		size = AndroidHelper.getInt(json, key_size, 0);
		fileUrl = AndroidHelper.getString(json, key_fileUrl, null);
		mimeType = AndroidHelper.getString(json, key_mimeType, null);
		thumbnailUrl = AndroidHelper.getString(json, key_thumbnailUrl, null);
		thumbnailWidth = AndroidHelper.getInt(json, key_thumbnailWidth, 0);
		thumbnailHeight = AndroidHelper.getInt(json, key_thumbnailHeight, 0);
	}
	public FileAntBuddy(AntBuddyFile antBuddyFile) {
		name = antBuddyFile.getName();
		size = antBuddyFile.getSize();
		fileUrl = antBuddyFile.getFileUrl();
		mimeType = antBuddyFile.getMimeType();
		thumbnailUrl = antBuddyFile.getThumbnailUrl();
	}

	public FileAntBuddy(Parcel in) {
		name = in.readString();
		size = in.readInt();
		fileUrl = in.readString();
		mimeType = in.readString();
		thumbnailUrl = in.readString();
		thumbnailWidth = in.readInt();
		thumbnailHeight = in.readInt();
	}

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel outParcel, int i) {
        outParcel.writeString(name);
        outParcel.writeInt(size);
        outParcel.writeString(fileUrl);
        outParcel.writeString(mimeType);
        outParcel.writeString(thumbnailUrl);
        outParcel.writeInt(thumbnailWidth);
        outParcel.writeInt(thumbnailHeight);
    }

    public static final Creator<FileAntBuddy> CREATOR = new Creator<FileAntBuddy>() {

        @Override
        public FileAntBuddy createFromParcel(Parcel in) {
            return new FileAntBuddy(in);
        }

        @Override
        public FileAntBuddy[] newArray(int size) {
            return new FileAntBuddy[size];
        }
    };

    public String getAllInformation() {
        String t = ("Name=" + name + ", size=" + size + ", fileURL=" + fileUrl + ", mimeType=" + mimeType + "thumbnaiUrl=" + thumbnailUrl);
        return t;
    }
}
