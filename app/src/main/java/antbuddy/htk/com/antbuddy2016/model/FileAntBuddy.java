package antbuddy.htk.com.antbuddy2016.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FileAntBuddy implements Parcelable {

	public static final String TAG_THISCLASS = "FileAntBuddy";
	private String name;           //google_play_services.zip
	private int size;		   //16223844
	private String fileUrl;        //http://s3-ap-southeast-1.amazonaws.com/hipchat/google_play_services1415098114018_google_play_services.zip
	private String mimeType;       //application/octet-stream
	private String thumbnailUrl;
	
	public FileAntBuddy() {
	}
	
	public FileAntBuddy(String name, int size, String fileUrl, String mimeType, String thumbnailUrl) {
		this.name = name;
		this.size = size;
		this.fileUrl = fileUrl;
		this.mimeType = mimeType;
		this.thumbnailUrl = thumbnailUrl;
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
    }

    public static final Creator<FileAntBuddy> CREATOR = new Creator<FileAntBuddy>() {

        @Override
        public FileAntBuddy createFromParcel(Parcel in) {
            String name = in.readString();
            int size = in.readInt();
            String fileUrl = in.readString();
            String mimeType = in.readString();
            String thumbnailUrl = in.readString();

            return new FileAntBuddy(name, size, fileUrl, mimeType, thumbnailUrl);
            //return new FileAntBuddy(name, "", "", "", "");
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
