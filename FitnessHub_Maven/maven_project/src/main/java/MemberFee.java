public class MemberFee {
    private String name;
    private boolean feePaid;

    public MemberFee(String name, boolean feePaid) {
        this.name = name;
        this.feePaid = feePaid;
    }

    public String getName() {
        return name;
    }

    public boolean isFeePaid() {
        return feePaid;
    }

    @Override
    public String toString() {
        return "Member Name: " + name + ", Fee Paid: " + feePaid;
    }
}

