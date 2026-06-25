public class Membership {
    private String membershipType;
    private String duration;
    private int price;
    private String features;

    public Membership(String membershipType, String duration, int price, String features) {
        this.membershipType = membershipType;
        this.duration = duration;
        this.price = price;
        this.features = features;
    }

    // Getters
    public String getMembershipType() { return membershipType; }
    public String getDuration() { return duration; }
    public int getPrice() { return price; }
    public String getFeatures() { return features; }

    @Override
    public String toString() {
        return "MembershipType: " + membershipType +
                ", Duration: " + duration +
                ", Price: " + price +
                ", Features: " + features;
    }
}