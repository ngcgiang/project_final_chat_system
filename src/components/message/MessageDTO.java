package components.message;

import java.sql.Timestamp;

public class MessageDTO {
    private int messageID; // Mã tin nhắn
    private int senderID; // ID người gửi
    private int receiverID; // ID người nhận
    private String content; // Nội dung tin nhắn
    private Timestamp sentAt; // Thời gian gửi

    // Constructors
    public MessageDTO() {
    }

    public MessageDTO(int messageID, int senderID, int receiverID, String content, Timestamp sentAt) {
        this.messageID = messageID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.content = content;
        this.sentAt = sentAt;
    }

    // Getters and Setters
    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "MessageDTO{" +
                "messageID=" + messageID +
                ", senderID=" + senderID +
                ", receiverID=" + receiverID +
                ", content='" + content + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}
