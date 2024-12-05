package components.admin.user_friends;

import java.sql.SQLException;
import java.util.List;

public class UserFriendBUS {

    private UserFriendDAO userFriendDAO;

    public UserFriendBUS() {
        this.userFriendDAO = new UserFriendDAO();
    }

    public List<UserFriendDTO> getAllUserFriends() throws SQLException {
        return userFriendDAO.getAllUserFriends();
    }

    public List<UserFriendDTO> getUserFriendsByUserId(int userId) throws SQLException {
        return userFriendDAO.getUserFriendsByUserId(userId);
    }

    public List<UserFriendDTO> getFilteredUserFriends(String sortBy, String comparison, String compareTo, String username) throws SQLException {
        return userFriendDAO.getFilteredUserFriends(sortBy, comparison, compareTo, username);
    }
}
