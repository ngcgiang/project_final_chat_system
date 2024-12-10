package components.user.relationship;

public class RelationshipBUS {
    private RelationshipDAO relationshipDAO;

    public RelationshipBUS() {
        relationshipDAO = new RelationshipDAO();
    }

    public boolean addFriend(String username1, String username2) {
        return relationshipDAO.addFriendRequest(username1, username2);
    }

    public boolean acceptFriendRequest(String username1, String username2) {
        relationshipDAO.removeFriendRequest(username1, username2);
        return relationshipDAO.addFriendship(username1, username2);
    }

    public boolean rejectOrCancelFriendRequest(String username1, String username2) {
        return relationshipDAO.removeFriendRequest(username1, username2);
    }

    public boolean unfriend(String username1, String username2) {
        return relationshipDAO.removeFriendship(username1, username2);
    }

    public boolean block(String username1, String username2) {
        relationshipDAO.removeFriendship(username1, username2);
        return relationshipDAO.addBlockingRelationship(username1, username2);
    }
}
