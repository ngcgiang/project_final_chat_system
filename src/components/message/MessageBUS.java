package components.message;

import java.util.ArrayList;

public class MessageBUS {
    private MessageDAO messageDAO;

    public MessageBUS() {
        messageDAO = new MessageDAO();
    }

    public boolean saveMessage(String sender, String receiver, String content) {
        return messageDAO.saveMessage(sender, receiver, content);
    }

    public ArrayList<MessageDTO> getMessages(String username1, String username2) {
        return messageDAO.getMessages(username1, username2);
    }
}
