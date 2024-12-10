package components.user.group;

import java.util.ArrayList;

import components.shared.utils.Response;
import components.user.UserBUS;

public class GroupBUS {
    private GroupDAO groupDAO;

    public GroupBUS() {
        groupDAO = new GroupDAO();
    }

    public GroupDTO getGroup(int groupID) {
        return groupDAO.getGroupInfo(groupID);
    }

    public int addGroup(GroupDTO groupDTO, ArrayList<Integer> memberIDList) {
        int groupID = groupDAO.addGroupInfo(groupDTO);
        if (groupID > 0) {
            boolean success = groupDAO.addGroupMembers(groupID, memberIDList);
            if (success) {
                return groupID;
            }
        }
        return -1;
    }

    public boolean updateGroup(GroupDTO groupDTO) {
        return groupDAO.updateGroupInfo(groupDTO);
    }

    public Response addMember(int groupID, String username) {
        UserBUS userBUS = new UserBUS();
        if (userBUS.getAccountInfo(username) == null)
            return new Response(false, "Username is not exist");
        return groupDAO.addGroupMember(groupID, username) ? new Response(true, "Add successful")
                : new Response(false, "This person was in the group");
    }

    public Response assignAdmin(GroupDTO groupDTO, String username) {
        UserBUS userBUS = new UserBUS();
        if (userBUS.getAccountInfo(username) == null)
            return new Response(false, "Username is not exist");
        ArrayList<Integer> groupMemberList = groupDAO.getGroupMemberList(groupDTO.getGroupID());
        int userID = userBUS.getAccountInfo(username).getID();
        for (int memberID : groupMemberList) {
            if (userID == memberID) {
                groupDTO.setAdminID(userID);
                groupDAO.updateGroupInfo(groupDTO);
                return new Response(true, "Assign admin to " + username + " successful!");
            }
        }
        return new Response(false, "This person is not in group");
    }

    public Response removeMember(int groupID, String username) {
        UserBUS userBUS = new UserBUS();
        if (userBUS.getAccountInfo(username) == null)
            return new Response(false, "Username is not exist");

        ArrayList<Integer> groupMemberList = groupDAO.getGroupMemberList(groupID);
        int userID = userBUS.getAccountInfo(username).getID();
        for (int memberID : groupMemberList) {
            if (userID == memberID) {
                groupDAO.removeGroupMember(groupID, username);
                return new Response(true, "Remove " + username + " from group successful!");
            }
        }
        return new Response(false, "This person is not in group");
    }
}
