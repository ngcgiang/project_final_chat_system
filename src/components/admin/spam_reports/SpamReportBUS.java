package components.admin.spam_reports;

import java.util.List;

public class SpamReportBUS {
    private final SpamReportDAO dao;

    public SpamReportBUS() {
        dao = new SpamReportDAO();
    }

    public List<SpamReportDTO> getSpamReports(String sortBy, String timeFilter, String usernameFilter) {
        return dao.getSpamReports(sortBy, timeFilter, usernameFilter);
    }

    public boolean toggleUserAccess(int userId, String currentAccess) {
        String newAccess = currentAccess.equals("Yes") ? "No" : "Yes";
        return dao.updateUserAccess(userId, newAccess);
    }
}
