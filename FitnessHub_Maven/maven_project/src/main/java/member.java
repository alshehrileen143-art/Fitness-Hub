import javafx.beans.property.SimpleStringProperty;

public class member {
    private static SimpleStringProperty username = new SimpleStringProperty();

    // Constructor (doesn't change username, just allows object creation)
    public member() {
        // No need to set username in the constructor
    }

    // Method to set the username (set once and applies to all instances)
    public static void setUsername(String name) {
        username.set(name);
    }

    // Getter for username
    public static String getUsername() {
        return username.get();
    }

    // Property method for JavaFX bindings
    public static SimpleStringProperty usernameProperty() {
        return username;
    }
}
