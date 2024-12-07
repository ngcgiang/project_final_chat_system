package components.conversation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import view.user.*;
import components.user.*;

public class ConversationBUS {
    private ConversationDAO conversationDAO;

    public ConversationBUS() {
        conversationDAO = new ConversationDAO();
    }

    public ArrayList<Message.Conversation> getConversations(String username) {
        ArrayList<Message.Conversation> conversations = new ArrayList<>();
        ArrayList<ConversationDTO> conversationDTOList = conversationDAO.getConversationList(username);

        for (ConversationDTO dto : conversationDTOList) {
            // Tạo instance của lớp Message.Conversation
            Message.Conversation conversation = new Message.Conversation();

            // Lấy tên bạn bè từ FriendID
            UserBUS userBUS = new UserBUS();
            String friendUsername = userBUS.getUsernameByID(dto.getFriendID());

            conversation.setFriendUsername(friendUsername);
            conversation.setFriendName(userBUS.getAccountInfo(friendUsername).getFullName());
            conversation.setLastMessage(dto.getLastMessage());

            // Định dạng lại thời gian
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String formattedTime = dateFormat.format(dto.getLastMessageTime());
            conversation.setTime(formattedTime);

            conversations.add(conversation);
        }
        return conversations;
    }

    public boolean addOrUpdateConversation(String username1, String username2, String lastMessage) {
        return conversationDAO.addOrUpdateConversation(username1, username2, lastMessage);
    }
}
