import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MarkdownDocument implements Document {
    public String getType() { return "Markdown"; }
    
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
        return String.format(
            "# %s\n" +
            "### %s\n" +
            "#### %s\n" +
            "%s",
            title, author, StringUtils.dateToString(date), content
        );
    }
    public boolean fromString(String str) {
        StringParser spFile = new StringParser(str);
        
        if (!spFile.trim().extractStartsContainer("# ", "\n")) return false;
        String title = spFile.getContainer();
        
        if (!spFile.trim().extractStartsContainer("### ", "\n")) return false;
        String author = spFile.getContainer();
        
        if (!spFile.trim().extractStartsContainer("#### ", "\n")) return false;
        String date = spFile.getContainer();

        String content = spFile.getStr().trim();
        
        try {
            this.date = StringUtils.dateFromString(date);
        } catch (DateTimeParseException e) { return false; }
        this.title   = title;
        this.author  = author;
        this.content = content;
        return true;
    }
}
