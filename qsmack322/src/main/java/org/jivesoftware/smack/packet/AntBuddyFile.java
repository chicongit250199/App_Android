package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.util.StringUtils;

/**
 * Created by htk on 26/12/2014.
 */
public class AntBuddyFile {

    private String name;           //google_play_services.zip
    private int size;		   //16223844
    private String fileUrl;        //http://s3-ap-southeast-1.amazonaws.com/hipchat/google_play_services1415098114018_google_play_services.zip
    private String mimeType;       //application/octet-stream
    private String thumbnailUrl;

    public AntBuddyFile(String name, int size, String fileUrl, String mimeType, String thumbnailUrl) {
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

    public String toXML() {
        StringBuilder buf = new StringBuilder();
        buf.append("<file>");
        if (getName() != null) {
            buf.append("<name>").append(StringUtils.escapeForXML(getName())).append("</name>");
        }
        buf.append("<size>").append(StringUtils.escapeForXML(getSize() + "")).append("</size>");
        if (getFileUrl() != null) {
            buf.append("<fileUrl>").append(StringUtils.escapeForXML(getFileUrl())).append("</fileUrl>");
        }
        if (getMimeType() != null) {
            buf.append("<mimeType>").append(StringUtils.escapeForXML(getMimeType())).append("</mimeType>");
        }
        if (getThumbnailUrl() != null) {
            buf.append("<thumbnailUrl>").append(StringUtils.escapeForXML(getThumbnailUrl())).append("</thumbnailUrl>");
        }
        buf.append("</file>");
        return buf.toString();
    }
}
