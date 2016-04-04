/**
 * $RCSfile$
 * $Revision: $
 * $Date: $
 *
 * Copyright 2003-2006 Jive Software.
 *
 * All rights reserved. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jivesoftware.smackx.packet;

import java.util.Date;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.StringUtils;

/**
 * The process by which two entities initiate a stream.
 *
 * @author Alexander Wenckus
 */
public class StreamInitiation extends IQ {

    private String id;

    private String mimeType;

    private File file;

    private Feature featureNegotiation;

    /**
     * The "id" attribute is an opaque identifier. This attribute MUST be
     * present on type='set', and MUST be a valid string. This SHOULD NOT be
     * sent back on type='result', since the <iq/> "id" attribute provides the
     * only context needed. This value is generated by the Sender, and the same
     * value MUST be used throughout a session when talking to the Receiver.
     *
     * @param id The "id" attribute.
     */
    public void setSesssionID(final String id) {
        this.id = id;
    }

    /**
     * Uniquely identifies a stream initiation to the recipient.
     *
     * @return The "id" attribute.
     * @see #setSesssionID(String)
     */
    public String getSessionID() {
        return id;
    }

    /**
     * The "mime-type" attribute identifies the MIME-type for the data across
     * the stream. This attribute MUST be a valid MIME-type as registered with
     * the Internet Assigned Numbers Authority (IANA) [3] (specifically, as
     * listed at <http://www.iana.org/assignments/media-types>). During
     * negotiation, this attribute SHOULD be present, and is otherwise not
     * required. If not included during negotiation, its value is assumed to be
     * "binary/octect-stream".
     *
     * @param mimeType The valid mime-type.
     */
    public void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Identifies the type of file that is desired to be transfered.
     *
     * @return The mime-type.
     * @see #setMimeType(String)
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Sets the file which contains the information pertaining to the file to be
     * transfered.
     *
     * @param file The file identified by the stream initiator to be sent.
     */
    public void setFile(final File file) {
        this.file = file;
    }

    /**
     * Returns the file containing the information about the request.
     *
     * @return Returns the file containing the information about the request.
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the data form which contains the valid methods of stream neotiation
     * and transfer.
     *
     * @param form The dataform containing the methods.
     */
    public void setFeatureNegotiationForm(final DataForm form) {
        this.featureNegotiation = new Feature(form);
    }

    /**
     * Returns the data form which contains the valid methods of stream
     * neotiation and transfer.
     *
     * @return Returns the data form which contains the valid methods of stream
     *         neotiation and transfer.
     */
    public DataForm getFeatureNegotiationForm() {
        return featureNegotiation.getData();
    }

    /*
      * (non-Javadoc)
      *
      * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
      */
    public String getChildElementXML() {
        StringBuilder buf = new StringBuilder();
        if (this.getType().equals(IQ.Type.SET)) {
            buf.append("<si xmlns=\"http://jabber.org/protocol/si\" ");
            if (getSessionID() != null) {
                buf.append("id=\"").append(getSessionID()).append("\" ");
            }
            if (getMimeType() != null) {
                buf.append("mime-type=\"").append(getMimeType()).append("\" ");
            }
            buf
                    .append("profile=\"http://jabber.org/protocol/si/profile/file-transfer\">");

            // Add the file section if there is one.
            String fileXML = file.toXML();
            if (fileXML != null) {
                buf.append(fileXML);
            }
        }
        else if (this.getType().equals(IQ.Type.RESULT)) {
            buf.append("<si xmlns=\"http://jabber.org/protocol/si\">");
        }
        else {
            throw new IllegalArgumentException("IQ Type not understood");
        }
        if (featureNegotiation != null) {
            buf.append(featureNegotiation.toXML());
        }
        buf.append("</si>");
        return buf.toString();
    }

    /**
     * <ul>
     * <li>size: The size, in bytes, of the data to be sent.</li>
     * <li>name: The name of the file that the Sender wishes to send.</li>
     * <li>date: The last modification time of the file. This is specified
     * using the DateTime profile as described in Jabber Date and Time Profiles.</li>
     * <li>hash: The MD5 sum of the file contents.</li>
     * </ul>
     * <p/>
     * <p/>
     * &lt;desc&gt; is used to provide a sender-generated description of the
     * file so the receiver can better understand what is being sent. It MUST
     * NOT be sent in the result.
     * <p/>
     * <p/>
     * When &lt;range&gt; is sent in the offer, it should have no attributes.
     * This signifies that the sender can do ranged transfers. When a Stream
     * Initiation result is sent with the <range> element, it uses these
     * attributes:
     * <p/>
     * <ul>
     * <li>offset: Specifies the position, in bytes, to start transferring the
     * file data from. This defaults to zero (0) if not specified.</li>
     * <li>length - Specifies the number of bytes to retrieve starting at
     * offset. This defaults to the length of the file from offset to the end.</li>
     * </ul>
     * <p/>
     * <p/>
     * Both attributes are OPTIONAL on the &lt;range&gt; element. Sending no
     * attributes is synonymous with not sending the &lt;range&gt; element. When
     * no &lt;range&gt; element is sent in the Stream Initiation result, the
     * Sender MUST send the complete file starting at offset 0. More generally,
     * data is sent over the stream byte for byte starting at the offset
     * position for the length specified.
     *
     * @author Alexander Wenckus
     */
    public static class File implements PacketExtension {

        private final String name;

        private final long size;

        private String hash;

        private Date date;

        private String desc;

        private boolean isRanged;

        /**
         * Constructor providing the name of the file and its size.
         *
         * @param name The name of the file.
         * @param size The size of the file in bytes.
         */
        public File(final String name, final long size) {
            if (name == null) {
                throw new NullPointerException("name cannot be null");
            }

            this.name = name;
            this.size = size;
        }

        /**
         * Returns the file's name.
         *
         * @return Returns the file's name.
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the file's size.
         *
         * @return Returns the file's size.
         */
        public long getSize() {
            return size;
        }

        /**
         * Sets the MD5 sum of the file's contents
         *
         * @param hash The MD5 sum of the file's contents.
         */
        public void setHash(final String hash) {
            this.hash = hash;
        }

        /**
         * Returns the MD5 sum of the file's contents
         *
         * @return Returns the MD5 sum of the file's contents
         */
        public String getHash() {
            return hash;
        }

        /**
         * Sets the date that the file was last modified.
         *
         * @param date The date that the file was last modified.
         */
        public void setDate(Date date) {
            this.date = date;
        }

        /**
         * Returns the date that the file was last modified.
         *
         * @return Returns the date that the file was last modified.
         */
        public Date getDate() {
            return date;
        }

        /**
         * Sets the description of the file.
         *
         * @param desc The description of the file so that the file receiver can
         *             know what file it is.
         */
        public void setDesc(final String desc) {
            this.desc = desc;
        }

        /**
         * Returns the description of the file.
         *
         * @return Returns the description of the file.
         */
        public String getDesc() {
            return desc;
        }

        /**
         * True if a range can be provided and false if it cannot.
         *
         * @param isRanged True if a range can be provided and false if it cannot.
         */
        public void setRanged(final boolean isRanged) {
            this.isRanged = isRanged;
        }

        /**
         * Returns whether or not the initiator can support a range for the file
         * tranfer.
         *
         * @return Returns whether or not the initiator can support a range for
         *         the file tranfer.
         */
        public boolean isRanged() {
            return isRanged;
        }

        public String getElementName() {
            return "file";
        }

        public String getNamespace() {
            return "http://jabber.org/protocol/si/profile/file-transfer";
        }

        public String toXML() {
            StringBuilder buffer = new StringBuilder();

            buffer.append("<").append(getElementName()).append(" xmlns=\"")
                    .append(getNamespace()).append("\" ");

            if (getName() != null) {
                buffer.append("name=\"").append(StringUtils.escapeForXML(getName())).append("\" ");
            }

            if (getSize() > 0) {
                buffer.append("size=\"").append(getSize()).append("\" ");
            }

            if (getDate() != null) {
                buffer.append("date=\"").append(StringUtils.formatXEP0082Date(date)).append("\" ");
            }

            if (getHash() != null) {
                buffer.append("hash=\"").append(getHash()).append("\" ");
            }

            if ((desc != null && desc.length() > 0) || isRanged) {
                buffer.append(">");
                if (getDesc() != null && desc.length() > 0) {
                    buffer.append("<desc>").append(StringUtils.escapeForXML(getDesc())).append("</desc>");
                }
                if (isRanged()) {
                    buffer.append("<range/>");
                }
                buffer.append("</").append(getElementName()).append(">");
            }
            else {
                buffer.append("/>");
            }
            return buffer.toString();
        }
    }

    /**
     * The feature negotiation portion of the StreamInitiation packet.
     *
     * @author Alexander Wenckus
     *
     */
    public class Feature implements PacketExtension {

        private final DataForm data;

        /**
         * The dataform can be provided as part of the constructor.
         *
         * @param data The dataform.
         */
        public Feature(final DataForm data) {
            this.data = data;
        }

        /**
         * Returns the dataform associated with the feature negotiation.
         *
         * @return Returns the dataform associated with the feature negotiation.
         */
        public DataForm getData() {
            return data;
        }

        public String getNamespace() {
            return "http://jabber.org/protocol/feature-neg";
        }

        public String getElementName() {
            return "feature";
        }

        public String toXML() {
            StringBuilder buf = new StringBuilder();
            buf
                    .append("<feature xmlns=\"http://jabber.org/protocol/feature-neg\">");
			buf.append(data.toXML());
			buf.append("</feature>");
			return buf.toString();
		}
	}
}
