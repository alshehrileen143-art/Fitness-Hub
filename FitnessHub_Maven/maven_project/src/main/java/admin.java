import java.util.List;

public class admin {
    private int adminId;
    private String username;
    private String password;

    public admin(int adminId, String username, String password) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
    }

    // ✅ Add New Member
    public boolean addMembership(String membership, String duration, Integer price,String features) {
       memberships m = new memberships();
       boolean b = m.addMemberships(membership, duration, price, features);
       return b;
    }

    // ✅ Remove Member
    public boolean removeMembership(String membership) {
        memberships m = new memberships();
        boolean b = m.removeMembership(membership);
        return b;

    }


    // ✅ View All Members
    public void viewMembers() {
        UserDAO dao = new UserDAO();
        List<MemberFee> members = dao.getAllMembers() ;

    }


}
