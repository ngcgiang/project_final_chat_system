package components.admin.login_history;

import java.util.List;

public class LoginHistoryBUS {
    private final LoginHistoryDAO loginHistoryDAO;

    public LoginHistoryBUS() {
        this.loginHistoryDAO = new LoginHistoryDAO();
    }

    public List<LoginHistoryDTO> getAllLoginHistory() {
        return loginHistoryDAO.getAllLoginHistory();
    }

    public List<LoginHistoryDTO> getUserLoginHistory(int userId) {
        return loginHistoryDAO.getUserLoginHistory(userId);
    }
}
