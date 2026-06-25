public class Booking {
    private String trainer;
    private int time;
    private int day;
    private String member;
    private String booked;

    // Constructor for trainer-specific bookings
    public Booking(int time, int day, String member, String booked) {
        this.time = time;
        this.day = day;
        this.member = member;
        this.booked = booked;
    }

    // Constructor for confirmed bookings
    public Booking(String trainer, int time, int day, String booked) {
        this.trainer = trainer;
        this.time = time;
        this.day = day;
        this.booked = booked;
    }

    // **New Constructor** for available slots (excluding "booked" and "member")
    public Booking(String trainer, int time, int day) {
        this.trainer = trainer;
        this.time = time;
        this.day = day;
    }

    // Getters
    public String getTrainer() { return trainer; }
    public int getTime() { return time; }
    public int getDay() { return day; }
    public String getMember() { return member; }
    public String getBooked() { return booked; }
}
