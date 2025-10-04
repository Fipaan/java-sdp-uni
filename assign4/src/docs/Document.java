import java.time.LocalDateTime;

public interface Document {
    String getType();

    String getTitle();
    void   setTitle(String title);
    
    String getAuthor();
    void   setAuthor(String author);
    
    String getContent();
    void   setContent(String content);
    
    LocalDateTime getDate();
    void          setDate(LocalDateTime date);
    
    String        toString();
    boolean       fromString(String str);
}
