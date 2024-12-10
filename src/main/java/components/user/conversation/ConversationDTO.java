package components.user.conversation;

import java.sql.Timestamp;

public class ConversationDTO {
    private int conversationID; // Mã cuộc hội thoại
    private int userID; // Mã người dùng
    private int friendID; // Mã bạn bè
    private String lastMessage; // Tin nhắn gần nhất
    private Timestamp lastMessageTime; // Thời gian của tin nhắn gần nhất

    // Constructors
    public ConversationDTO() {
    }

    public ConversationDTO(int conversationID, int userID, int friendID, String lastMessage,
            Timestamp lastMessageTime) {
        this.conversationID = conversationID;
        this.userID = userID;
        this.friendID = friendID;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    // Getters and Setters
    public int getConversationID() {
        return conversationID;
    }

    public void setConversationID(int conversationID) {
        this.conversationID = conversationID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getFriendID() {
        return friendID;
    }

    public void setFriendID(int friendID) {
        this.friendID = friendID;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Timestamp getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Timestamp lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "ConversationDTO{" +
                "conversationID=" + conversationID +
                ", userID=" + userID +
                ", friendID=" + friendID +
                ", lastMessage='" + lastMessage + '\'' +
                ", lastMessageTime=" + lastMessageTime +
                '}';
    }
}
