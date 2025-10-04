import java.time.LocalDateTime;

public class GenericDocument implements Document {
    public String getType() {
        throw new UnsupportedOperationException("Shouldn't be used for `GenericDocument` class");
    }
    
    protected String title = null;
    public String getTitle()           { return title;       }
    public void setTitle(String title) { this.title = title; }
    
    protected String author = null;
    public String getAuthor()            { return author;        }
    public void setAuthor(String author) { this.author = author; }
    
    protected String content = null;
    public String getContent()             { return content;         }
    public void setContent(String content) { this.content = content; }
    
    protected LocalDateTime date = null;
    public LocalDateTime getDate()          { return date;      }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String toString() {
        throw new UnsupportedOperationException("Shouldn't be used for `GenericDocument` class");
    }
    public boolean fromString(String str) {
        throw new UnsupportedOperationException("Shouldn't be used for `GenericDocument` class");
    }
}
