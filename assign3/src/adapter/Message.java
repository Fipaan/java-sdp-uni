import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private String username;
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    private String message;
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    private LocalDateTime timestamp;
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setTimestamp() { this.timestamp = LocalDateTime.now(); }

    public Message(String username, String message) {
        this.username  = username;
        this.message   = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
};
