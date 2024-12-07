package components.admin.user_activity;

import java.util.List;

public class UserActivityBUS {
    private UserActivityDAO userActivityDAO;

    public UserActivityBUS() {
        this.userActivityDAO = new UserActivityDAO();
    }

    public List<UserActivityDTO> getUserActivities(String sortBy, String dateRange, String comparison, String compareTo, String username) {
        return userActivityDAO.fetchUserActivities(sortBy, dateRange, comparison, compareTo, username);
    }

    public int[] getUserOnlineCountsByMonth(int year) {
        return userActivityDAO.fetchUserOnlineCountsByMonth(year);
    }
}
