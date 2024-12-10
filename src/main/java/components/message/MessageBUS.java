package components.message;

import components.user.UserBUS;
import java.util.ArrayList;

public class MessageBUS {
    private MessageDAO messageDAO;

    public MessageBUS() {
        messageDAO = new MessageDAO();
    }

    public boolean saveMessage(String sender, String receiver, String content) {
        return messageDAO.saveMessage(sender, receiver, content);
    }

    public boolean saveGroupMessage(String sender, int groupID, String content) {
        UserBUS userBUS = new UserBUS();
        int senderID = userBUS.getAccountInfo(sender).getID();
        return messageDAO.saveGroupMessage(senderID, groupID, content);
    }

    public ArrayList<MessageDTO> getMessages(String username1, String username2) {
        return messageDAO.getMessages(username1, username2);
    }

    public ArrayList<MessageDTO> getGroupMessages(int groupID) {
        return messageDAO.getGroupMessages(groupID);
    }

    public boolean deleteMessage(int messageID) {
        return messageDAO.deleteMessage(messageID);
    }

    public boolean deleteAllMessages(String username1, String username2) {
        return messageDAO.deleteAllMessages(username1, username2);
    }

    public boolean deleteGroupMessage(int messageID) {
        return messageDAO.deleteGroupMessage(messageID);
    }

    public boolean deleteAllGroupMessages(int groupID) {
        return messageDAO.deleteAllGroupMessages(groupID);
    }
}
