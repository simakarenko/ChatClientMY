package academy.prog;

public class UserStatusJsonMessages {
    private String text;

    public UserStatusJsonMessages(String userName) {
        text=userName;
    }

    public String getStatus() {
        return text;
    }
}
